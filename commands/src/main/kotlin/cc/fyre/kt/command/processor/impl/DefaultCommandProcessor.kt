package cc.fyre.kt.command.processor.impl

import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.CommandFunction
import cc.fyre.kt.command.CommandMap
import cc.fyre.kt.command.argument.flag.Flag
import cc.fyre.kt.command.argument.parameter.Parameter
import cc.fyre.kt.command.argument.parameter.ParameterConverter
import cc.fyre.kt.command.exception.CommandProcessException
import cc.fyre.kt.command.processor.CommandProcessor
import kotlin.reflect.KParameter
import kotlin.reflect.safeCast

class DefaultCommandProcessor(
    override val function: CommandFunction,
    override val commandMap: CommandMap<*>
): CommandProcessor {

    //TODO somewhere else?
    init {
        this.function.parameters.forEach{parameter -> parameter.annotations.forEach { annotation ->
            this.commandMap[annotation.annotationClass, parameter.type]?.onTypeRegister(annotation)
        }}
    }

    override suspend fun process(actor: CommandActor<*,*>,label: String,arguments: Array<out String>): suspend () -> MutableMap<KParameter,Any?> {
        return parameters@ {

            val parameters = hashMapOf<KParameter,Any?>()

            var argIndex = 0
            var paramIndex = 0
            var attempts = 0
            var flagsCount = 0
            var parameterCount = 0 // this is so we know all required parameters are provided

            while ((parameterCount < this.function.min || attempts != this.function.max) && argIndex < arguments.size) {

                var source = arguments[argIndex]

                // Check if the current argument is an option
                var option = if (source[0] == cc.fyre.kt.command.annotation.Flag.FLAG_CHAR) {
                    this.function.flagsByKey[source.lowercase()]
                } else {
                    null
                }

                if (option != null && option.hasPermission(actor)) {
                    // Handle options (e.g., flags)
                    flagsCount++
                    parameters[option.parameter] = true
                    argIndex++
                } else {

                    val argument = this.function.parameters[paramIndex]

                    val adapter = if (argument.vararg) {
                        this.commandMap[String::class]
                    } else {
                        this.commandMap[argument.type]
                    }

                    if (adapter == null) {
                        throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_INVALID,argument.typeName)
                    }

                    if (argument.wildcard) {

                        val conjoined = StringBuilder()

                        for (i in argIndex ..< arguments.size) {

                            val part = arguments[i]

                            option = if (source[0] == cc.fyre.kt.command.annotation.Flag.FLAG_CHAR) {
                                this.function.flagsByKey[part.lowercase()]
                            } else {
                                null
                            }

                            if (option != null && option.hasPermission(actor)) {
                                flagsCount++
                                parameters[option.parameter] = true
                                continue
                            }

                            if (i != argIndex) {
                                conjoined.append(" ")
                            }

                            conjoined.append(part)
                        }

                        source = conjoined.toString()

                        val converted = this.transform(actor.value,source,argument,adapter)

                        if (converted != null) {
                            parameters[argument.parameter] = converted
                            parameterCount++
                        } else {

                            if (!(argument.nullable /*|| argument.optional*/)) {
                                adapter.handleException(actor,source,null)
                                throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_CONVERSION)
                            }

                        }

                        break
                    } else if (argument.vararg) {
                        // Handle varargs
                        val separator = argument.separator ?: ','

                        if (!source.contains(separator)) {
                            throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_MISSING_SEPARATOR,separator)
                        }

                        // TODO
                        // the issue is that it will be passed as an array of <Any>
                        // but needs to be passed as an array of <T>
                        // resulting in it being a type mismatch
                        val converted = source.split(separator).map{this.transform(actor.value,it,argument,adapter)}.toTypedArray()

                        // Don't break here to continue processing remaining arguments
                        paramIndex++
                        parameterCount++

                        parameters[argument.parameter] = argument.type.safeCast(converted)
                    } else {

                        val converted = this.transform(actor.value,source,argument,adapter)

                        if (converted != null) {
                            parameters[argument.parameter] = converted
                            paramIndex++
                        } else {

                            if (!(argument.nullable /*|| argument.optional*/)) {
                                adapter.handleException(actor.value,source,null)
                                throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_CONVERSION)
                            }

                        }

                        parameterCount++
                    }

                    argIndex++
                }

                attempts++
            }

            if (this.function.flags.isNotEmpty() && flagsCount < this.function.flags.size && argIndex < arguments.size) {

                for (i in (argIndex + 1) until arguments.size) {

                    val source = arguments[i]
                    val option = if (source[0] == cc.fyre.kt.command.annotation.Flag.FLAG_CHAR) {
                        this.function.flagsByKey[source.lowercase()]
                    } else {
                        null
                    }

                    if (option != null && option.hasPermission(actor)) {
                        parameters[option.parameter] = true
                        continue
                    }

                }

            }

            if (parameterCount < this.function.min) {
                throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_COUNT_INSUFFICIENT,label,this.function.command.description)
            }

            for ((key,value) in this.function.defaults) {

                if (parameters.containsKey(key)) {
                    continue
                }

                parameters[key] = value
            }

            return@parameters parameters
        }
    }

    private suspend fun transform(actor: Any,source: String,parameter: Parameter,parameterConverter: ParameterConverter<*,*>):Any? {

        val annotations = parameter.annotations.associateWith{this.commandMap[it.annotationClass,parameter.type]}

        if (annotations.isNotEmpty()) {

            for ((annotation,annotationConverter) in annotations) {

                if (annotationConverter == null) {
                    throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.ANNOTATION_INVALID,annotation.annotationClass.simpleName!!)
                }

                if (!annotationConverter.preTransform(actor,source,annotation)) {
                    throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.ANNOTATION_CONVERSION)
                }

            }

        }

        var converted: Any? = try {
            parameterConverter.convert(actor,source,parameter.annotations)
        } catch (ex: Exception) {
            parameterConverter.handleException(actor,source,ex)
            throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.PARAMETER_CONVERSION)
        }

        if (annotations.isNotEmpty()) {

            for ((annotation,annotationConverter) in annotations) {

                if (annotationConverter == null) {
                    throw CommandProcessException(this.function.command, CommandProcessException.ErrorType.ANNOTATION_INVALID, annotation.annotationClass.simpleName!!)
                }

//                if (annotationConverter.nullable != (converted == null)) {
//                    println("Continue")
//                    continue
//                }

                converted = annotationConverter.postTransform(actor,source,converted,annotation) ?: throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.ANNOTATION_CONVERSION)
//                val continuation = annotationConverter.postTransform(actor,source,converted,annotation)
//
//                if (!continuation.resume) {
//                    throw CommandProcessException(this.function.command,CommandProcessException.ErrorType.ANNOTATION_CONVERSION)
//                }
//
//                converted = continuation.value
            }

        }

        return converted
    }

}