package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation

@Target(AnnotationTarget.CLASS,AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@CommandAnnotation
annotation class Permission(
    val permission: String,
)

