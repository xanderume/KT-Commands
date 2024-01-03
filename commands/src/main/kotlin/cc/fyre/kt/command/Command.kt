package cc.fyre.kt.command

import cc.fyre.kt.command.exception.CommandProcessException
import cc.fyre.kt.command.processor.CommandProcessor
import kotlin.math.min
import kotlin.reflect.full.callSuspendBy
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO

interface Command {

    val instance: Any

    val id: Int
    val name: String
    val label: String
    val aliases: Array<String>
    val children: CommandChildMap<*>
    val permission: String?
    val description: String?
    val cooldown: Long?

    val helper: CommandHelper<*>?
    val function: CommandFunction?
    val processor: CommandProcessor?
    val commandMap: CommandMap<*>

    suspend fun execute(actor: CommandActor<*,*>,label: String,arguments: Array<out String>) {

        val child = if (arguments.isNotEmpty()) {
            this.children[arguments[0]]
        } else {
            null
        }

        if (child != null && actor.hasPermission(child)) {
            return child.execute(actor,"$label ${arguments[0]}",arguments.copyOfRange(1,arguments.size))
        }

        if (this.permission != null && !actor.hasPermission(this)) {
            throw CommandProcessException(this,CommandProcessException.ErrorType.NO_PERMISSION)
        }

        val function = this.function?.function

        // TODO better approach
        if (function == null) {

            val helper = this.helper ?: throw CommandProcessException(this,CommandProcessException.ErrorType.NO_FUNCTION_OR_HELPER)

            val page = if (arguments.isNotEmpty()) {
                arguments[0].toIntOrNull() ?: 1
            } else {
                1
            }

            helper.sendMessage(actor,label,min(page,helper.pages))
            return
        }

        val cooldown = this.cooldown

        if (cooldown != null && cooldown != 0L) {

            val remaining = actor.getLastUsage(this) - cooldown

            if (remaining > 0L) {
                throw CommandProcessException(this,CommandProcessException.ErrorType.COOLDOWN,remaining)
            }

            actor.setLastUsage(this,System.currentTimeMillis())
        }

        val parameters = try {
            this.processor!!.process(actor,label,arguments).invoke()
        } catch (ex: CommandProcessException) {
            actor.onProcessException(this,label,ex)
            return
        }

        parameters[this.function!!.instanceParameter] = this.instance
        parameters[this.function!!.senderParameter] = actor.value

        if (this.function!!.suspending) {
            function.callSuspendBy(parameters)
        } else {
            function.callBy(parameters)
        }

    }

}