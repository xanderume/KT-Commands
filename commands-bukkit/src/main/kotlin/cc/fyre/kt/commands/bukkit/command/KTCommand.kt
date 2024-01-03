package cc.fyre.kt.commands.bukkit.command

import cc.fyre.kt.command.CommandExecutor
import cc.fyre.kt.command.annotation.Alias
import org.bukkit.command.CommandSender

@Alias(aliases = ["ktcommands","kt-commands"])
object KTCommand {

    @CommandExecutor
    fun execute(sender: CommandSender) {
        sender.sendMessage("https://github.com/xanderume/KT-Commands.git")
    }

}