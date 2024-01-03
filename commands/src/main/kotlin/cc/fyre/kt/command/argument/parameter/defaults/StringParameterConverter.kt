package cc.fyre.kt.command.argument.parameter.defaults

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import kotlin.reflect.KClass

object StringParameterConverter : ParameterConverter<String,Any> {

    override val type: KClass<out String> = String::class

    override suspend fun convert(actor: Any,source: String,annotations: Array<out Annotation>): String {
        return source
    }

    override suspend fun handleException(actor: Any,source: String,exception: Exception?) {}

}