package cc.fyre.kt.commands.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand

class BungeeCommandInvocation(
    private val sender: CommandSource,
    private val alias: String,
    private val arguments: Array<String>
) : SimpleCommand.Invocation {

    override fun source(): CommandSource {
        return this.sender
    }

    override fun arguments(): Array<String> {
        return this.arguments
    }

    override fun alias(): String {
        return this.alias
    }

}