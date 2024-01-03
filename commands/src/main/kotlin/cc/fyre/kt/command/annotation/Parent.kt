package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@CommandAnnotation
annotation class Parent(
    val parent: KClass<*>
)
