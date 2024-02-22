package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.annotation.Cooldown
import cc.fyre.kt.command.argument.Argument
import cc.fyre.kt.command.exception.CommandProcessException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

open class BukkitCommandActor(val sender: CommandSender) : CommandActor<CommandSender,BukkitCommand>(sender) {

    private val audience = Audience.sender(this.sender)

    override fun hasPermission(command: BukkitCommand): Boolean {

        if (command.permission != null) {
            return this.sender.hasPermission(command.permission)
        }

        if (command.hidden) {
            return this.sender.isOp
        }

        return true
    }

    override fun hasPermission(argument: Argument): Boolean {
        return argument.permission == null || this.sender.hasPermission(argument.permission!!)
    }

    override fun getLastUsage(command: BukkitCommand): Long {

        if (this.sender !is Player) {
            return lastUsages[command.id] ?: 0L
        }

        if (!this.sender.hasMetadata("$LastCommandUsagePrefix:${command.id}")) {
            return 0L
        }

        return this.sender.getMetadata("$LastCommandUsagePrefix:${command.id}")[0].asLong()
    }

    override fun setLastUsage(command: BukkitCommand, time: Long) {

        if (this.sender is Player) {
            this.sender.setMetadata("$LastCommandUsagePrefix:${command.id}",FixedMetadataValue(command.plugin,time))
            return
        }

        lastUsages[command.id] = time
    }

    override fun onProcessException(command: BukkitCommand,label: String,exception: CommandProcessException) {

        when (exception.error) {
            CommandProcessException.ErrorType.NO_FUNCTION_OR_HELPER -> {
                this.audience.sendMessage(CommandHasNoSubCommands)
            }
            CommandProcessException.ErrorType.NO_PERMISSION -> {
                this.audience.sendMessage(NoPermissionMessage)
            }
            CommandProcessException.ErrorType.COOLDOWN -> {
                this.audience.sendMessage(CooldownMessage)
            }
            CommandProcessException.ErrorType.PARAMETER_INVALID -> {
                this.audience.sendMessage(CommandExecutionFailedMessage)
                exception.printStackTrace()
            }
            CommandProcessException.ErrorType.ANNOTATION_INVALID -> {
                this.audience.sendMessage(CommandExecutionFailedMessage)
                exception.printStackTrace()
            }
            CommandProcessException.ErrorType.PARAMETER_MISSING_SEPARATOR -> {
                this.audience.sendMessage(CommandExecutionFailedMessage)
            }
            CommandProcessException.ErrorType.PARAMETER_COUNT_INSUFFICIENT -> {

                val component = Component.text()
                    .append(Component.text("Usage:"))
                    .appendSpace()
                    .append(Component.text("/${label}"))
                    .appendSpace()
                    .color(NamedTextColor.RED)
                    .append(BukkitCommandPlugin.adapter.createUsage(this,command))

                val description = command.description

                if (!description.isNullOrEmpty()) {
                    component.hoverEvent(HoverEvent.showText(Component.text(description).color(NamedTextColor.GRAY)))
                }

                Audience.sender(this.sender).sendMessage(component.build())
            }
            CommandProcessException.ErrorType.PARAMETER_CONVERSION,CommandProcessException.ErrorType.ANNOTATION_CONVERSION -> {}
        }

    }


    companion object {

        private val lastUsages = hashMapOf<Int,Long>()

    }

}