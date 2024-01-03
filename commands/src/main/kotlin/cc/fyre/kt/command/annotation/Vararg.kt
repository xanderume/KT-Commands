package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@CommandAnnotation
annotation class Vararg<T>(val type: KClass<Array<T>>)
