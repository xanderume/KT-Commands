package cc.fyre.kt.command

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import cc.fyre.kt.command.exception.CommandLoadException
import kotlin.reflect.KClass

interface CommandMap<T> : Iterable<T> {

    val defaultTabCompleter: CommandTabCompleter<*>

    operator fun get(command: String):T?
    operator fun get(source: Any):T?
    operator fun get(source: KClass<*>): ParameterConverter<*,*>?
    operator fun get(annotation: KClass<out Annotation>,type: KClass<*>): AnnotationConverter<*,*,*>?

    @Throws(CommandLoadException::class)
    fun register(command: Any,parent: Command? = null): Command

    @Throws(CommandLoadException::class)
    fun register(converter: ParameterConverter<*,*>,override: Boolean = false)

    @Throws(CommandLoadException::class)
    fun register(converter: AnnotationConverter<*,*,*>,vararg subTypes: KClass<*> = emptyArray())

}