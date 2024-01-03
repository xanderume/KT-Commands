package cc.fyre.kt.command.annotation

import cc.fyre.kt.command.CommandAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@CommandAnnotation
annotation class Separator(val value: Char = DEFAULT_VALUE) {

    companion object {

        const val DEFAULT_VALUE = ','
        const val ILLEGAL_VALUE = ' '

    }

}
