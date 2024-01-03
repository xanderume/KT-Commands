package cc.fyre.kt.command.argument.parameter.defaults

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import kotlin.reflect.KClass

abstract class NumberParameterConverter<T: Any> : ParameterConverter<Number,T> {

    override val type: KClass<out Number> = Number::class

    override suspend fun convert(actor: T,source: String,annotations: Array<out Annotation>): Number? {
        return source.trim().let{value ->
            try {
                value.toInt()
            } catch (ex: NumberFormatException) {
                value.toDouble()
            } catch (ex: NumberFormatException) {
                null
            }
        }
    }

}