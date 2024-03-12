package cc.fyre.kt.command.argument

import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.annotation.Description
import cc.fyre.kt.command.annotation.Permission
import java.util.Objects
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

abstract class Argument(
    val parameter: KParameter
) {

    val name: String = this.parameter.name ?: "null"

    val index: Int = this.parameter.index - 2 // Command and Sender first
    val methodIndex = this.parameter.index

    private var _permission: String? = this.parameter.findAnnotation<Permission>()?.permission

    val permission: String?
        get() = this._permission

    val description: String? = this.parameter.findAnnotation<Description>()?.description

    open val type: KClass<*> = this.parameter.type.classifier as KClass<*>

    abstract val optional: Boolean
    abstract val nullable: Boolean

    fun <A: CommandActor<*,*>> hasPermission(actor: A):Boolean {

        if (this.permission == null) {
            return true
        }

        return actor.hasPermission(this)
    }

    fun setPermission(permission: String) {
        this._permission = permission
    }

    override fun equals(other: Any?): Boolean {
        return other is Argument && this.methodIndex == other.methodIndex
    }

    override fun hashCode(): Int {
        return Objects.hashCode(this.methodIndex)
    }

}