package cc.fyre.kt.command

import cc.fyre.kt.command.argument.Argument
import cc.fyre.kt.command.argument.flag.Flag
import cc.fyre.kt.command.argument.parameter.Parameter
import cc.fyre.kt.command.exception.CommandLoadException
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType

open class CommandFunction(val command: Command,val function: KFunction<*>) {

    val senderParameter: KParameter = this.function.parameters[1]
    val instanceParameter: KParameter = this.function.parameters[0]

    val arguments = arrayListOf<Argument>()
    val suspending = this.function.isSuspend

    val flags = arrayListOf<Flag>()
    val flagsByKey = hashMapOf<String,Flag>()

    val parameters = arrayListOf<Parameter>()
    val defaults = hashMapOf<KParameter,Any?>()

    init {
        this.function.parameters.subList(2,this.function.parameters.size).forEach{ parameter ->

            val value: Argument

            when {
                parameter.hasAnnotation<cc.fyre.kt.command.annotation.Flag>() -> {

                    value = Flag(parameter)

                    if (value.type.starProjectedType != Boolean::class.starProjectedType) {
                        throw CommandLoadException(this.command,"Option arguments can only be a boolean!")
                    }

                    for (key in value.flags) {
                        this.flagsByKey["-${key.lowercase()}"] = value
                    }

                    this.flags.add(value)
                    this.defaults[value.parameter] = false
                }
                else -> {

                    value = Parameter(parameter)

                    if (value.separator != null && value.separator == ' ') {
                        throw CommandLoadException(this.command,"Space is an illegal separator (${parameter.name})")
                    }

                    if (value.wildcard && value.methodIndex != (this.function.parameters.size - 1)) {
                        throw CommandLoadException(this.command,"Wildcard parameters can only be used as last parameter (${parameter.name})")
                    }

//                    if (value.nullable) {
//                        this.defaults[value.parameter] = null
//                    }

                    this.parameters.add(value)
                }
            }

            this.arguments.add(value)
        }
    }

    val min: Int = this.parameters.count{!(it.optional || it.nullable)}
    val max: Int
        get() = this.parameters.size
}