package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation
import cc.fyre.kt.command.CommandTabCompleter
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@CommandAnnotation
annotation class TabCompleter(
    val tabCompleter: KClass<out CommandTabCompleter<*>>,
)