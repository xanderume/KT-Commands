package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@CommandAnnotation
annotation class Flag(
    val flags: Array<String>
) {

    companion object {

        const val FLAG_CHAR = '-'

    }

}