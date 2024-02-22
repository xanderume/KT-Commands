package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.argument.Argument
import cc.fyre.kt.command.exception.CommandProcessException
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import io.github.reactivecircus.cache4k.Cache
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

open class BungeeCommandActor(val sender: CommandSource) : CommandActor<CommandSource,BungeeCommand>(sender) {

    override fun hasPermission(command: BungeeCommand): Boolean {

        if (command.permission != null || command.hidden) {
            return this.sender.hasPermission(command.permission)
        }

//        if (command.hidden) {
//            return this.sender.isOp
//        }

        return true
    }

    override fun hasPermission(argument: Argument): Boolean {
        return argument.permission == null || this.sender.hasPermission(argument.permission!!)
    }

    override fun getLastUsage(command: BungeeCommand): Long {

        if (this.sender !is Player) {
            return lastConsoleUsages[command.id] ?: 0L
        }

        val map = lastPlayerUsages.get(this.sender.uniqueId)

        if (map == null) {
            return 0L
        }

        return map[command.id] ?: 0L
    }

    override fun setLastUsage(command: BungeeCommand, time: Long) {

        if (this.sender !is Player) {
            lastConsoleUsages[command.id] = time
            return
        }

        var map = lastPlayerUsages.get(this.sender.uniqueId)

        if (map == null) {
            map = hashMapOf()
            lastPlayerUsages.put(this.sender.uniqueId,map)
        }

        map[command.id] = time
    }

    override fun onProcessException(command: BungeeCommand,label: String,exception: CommandProcessException) {

        when (exception.error) {
            CommandProcessException.ErrorType.NO_FUNCTION_OR_HELPER -> {
                this.sender.sendMessage(CommandHasNoSubCommands)
            }
            CommandProcessException.ErrorType.NO_PERMISSION -> {
                this.sender.sendMessage(NoPermissionMessage)
            }
            CommandProcessException.ErrorType.COOLDOWN -> {
                this.sender.sendMessage(CooldownMessage)
            }
            CommandProcessException.ErrorType.PARAMETER_INVALID -> {
                this.sender.sendMessage(CommandExecutionFailedMessage)
                exception.printStackTrace()
            }
            CommandProcessException.ErrorType.ANNOTATION_INVALID -> {
                this.sender.sendMessage(CommandExecutionFailedMessage)
                exception.printStackTrace()
            }
            CommandProcessException.ErrorType.PARAMETER_MISSING_SEPARATOR -> {
                this.sender.sendMessage(CommandExecutionFailedMessage)
            }
            CommandProcessException.ErrorType.PARAMETER_COUNT_INSUFFICIENT -> {

                val component = Component.text()
                    .append(Component.text("Usage:"))
                    .appendSpace()
                    .append(Component.text("/${label}"))
                    .appendSpace()
                    .color(NamedTextColor.RED)
                    .append(BungeeCommandPlugin.adapter.createUsage(this,command))

                val description = command.description

                if (!description.isNullOrEmpty()) {
                    component.hoverEvent(HoverEvent.showText(Component.text(description).color(NamedTextColor.GRAY)))
                }

                this.sender.sendMessage(component.build())
            }
            CommandProcessException.ErrorType.PARAMETER_CONVERSION, CommandProcessException.ErrorType.ANNOTATION_CONVERSION -> {}
        }

    }


    companion object {

        private val lastPlayerUsages = Cache.Builder<UUID,HashMap<Int,Long>>()
            .expireAfterAccess(5L.minutes)
            .build()

        private val lastConsoleUsages = hashMapOf<Int,Long>()

    }

}