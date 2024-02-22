package cc.fyre.kt.commands.velocity.command

import cc.fyre.kt.command.CommandExecutor
import cc.fyre.kt.command.annotation.Alias
import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.Component

@Alias(aliases = ["vktcommands","vkt-commands"])
object KTCommand {

    @CommandExecutor
    fun execute(sender: CommandSource) {
        sender.sendMessage(Component.text("https://github.com/xanderume/KT-Commands.git"))
    }

}