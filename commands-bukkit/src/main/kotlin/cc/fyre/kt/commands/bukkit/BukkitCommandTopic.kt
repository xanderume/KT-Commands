package cc.fyre.kt.commands.bukkit

import org.bukkit.command.CommandSender
import org.bukkit.help.GenericCommandHelpTopic

class BukkitCommandTopic(command: BukkitCommand) : GenericCommandHelpTopic(command.wrapper) {

    override fun canSee(sender: CommandSender): Boolean {

        val command = this.command

        if (command !is BukkitCommandWrapper || !command.hidden) {
            return super.canSee(sender)
        }

        if (command.permission != null) {
            return sender.hasPermission(command.permission!!)
        }

        return sender.isOp
    }

}