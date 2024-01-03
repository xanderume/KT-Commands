package cc.fyre.kt.command.argument.parameter.defaults

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import kotlin.reflect.KClass

abstract class IntParameterConverter<T: Any> : ParameterConverter<Int, T> {

    override val type: KClass<out Int> = Int::class

    override suspend fun convert(actor: T,source: String,annotations: Array<out Annotation>): Int? {
        return source.toIntOrNull()
    }

}