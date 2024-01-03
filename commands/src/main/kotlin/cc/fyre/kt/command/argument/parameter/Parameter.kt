package cc.fyre.kt.command.argument.parameter

import cc.fyre.kt.command.CommandAnnotation
import cc.fyre.kt.command.CommandTabCompleter
import cc.fyre.kt.command.annotation.Separator
import cc.fyre.kt.command.annotation.TabCompleter
import cc.fyre.kt.command.annotation.Vararg
import cc.fyre.kt.command.annotation.Wildcard
import cc.fyre.kt.command.argument.Argument
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class Parameter(
    parameter: KParameter,
) : Argument(parameter) {

    val vararg: Boolean = this.parameter.isVararg
    val wildcard: Boolean = this.parameter.hasAnnotation<Wildcard>()
    val separator: Char? = this.parameter.findAnnotation<Separator>()?.value

    val annotations: Array<Annotation> = this.parameter.annotations.filter{!it.annotationClass.hasAnnotation<CommandAnnotation>()}.toTypedArray()
    var tabCompleter: CommandTabCompleter<*>? = this.parameter.findAnnotation<TabCompleter>()?.tabCompleter?.objectInstance

    override val type: KClass<*> = if (this.vararg) {
        this.parameter.findAnnotation<Vararg<*>>()!!.type
    } else {
        super.type
    }

    val typeName: String = this.type.simpleName!!

    override val optional: Boolean = this.parameter.isOptional
    override val nullable: Boolean = this.parameter.type.isMarkedNullable

}