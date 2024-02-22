package cc.fyre.kt.command.argument.flag

import cc.fyre.kt.command.annotation.Flag
import cc.fyre.kt.command.argument.Argument
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

class Flag(
    parameter: KParameter
) : Argument(parameter) {

    val flags = this.parameter.findAnnotation<Flag>()!!.flags
    val flag = this.flags[0]

    override val optional: Boolean = true
    override val nullable: Boolean = true

}