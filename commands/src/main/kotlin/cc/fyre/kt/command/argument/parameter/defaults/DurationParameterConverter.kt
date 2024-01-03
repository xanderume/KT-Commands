package cc.fyre.kt.command.argument.parameter.defaults

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import kotlin.reflect.KClass
import kotlin.time.Duration

abstract class DurationParameterConverter<T: Any> : ParameterConverter<Duration, T> {

    override val type: KClass<out Duration> = Duration::class

    override suspend fun convert(actor: T,source: String,annotations: Array<out Annotation>): Duration? {
        return Duration.parseOrNull(source)
    }

}