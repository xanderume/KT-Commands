package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation
import cc.fyre.kt.command.CommandHelper
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@CommandAnnotation
annotation class Helper(
    val helper: KClass<out CommandHelper<*>>
)
