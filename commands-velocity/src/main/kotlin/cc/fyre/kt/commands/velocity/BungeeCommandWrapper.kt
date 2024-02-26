package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.CommandScope
import cc.fyre.kt.command.exception.CommandProcessException
import com.github.shynixn.mccoroutine.velocity.SuspendingSimpleCommand
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import kotlinx.coroutines.withContext

class BungeeCommandWrapper(private val command: BungeeCommand) : SuspendingSimpleCommand {

    override suspend fun execute(invocation: SimpleCommand.Invocation) {

        val label = invocation.alias()
        val sender = invocation.source()
        val arguments = invocation.arguments()

        if (this.command.consoleOnly && sender !is ConsoleCommandSource) {
            sender.sendMessage(ConsoleOnlyMessage)
            return
        } else if (!this.command.executableByConsole && sender !is Player) {
            sender.sendMessage(PlayerOnlyMessage)
            return
        }

        withContext(CommandScope.coroutineContext) {

            val actor = BungeeCommandPlugin.adapter.createActor(sender)

            try {
                this@BungeeCommandWrapper.command.execute(actor,label,arguments)
            } catch (ex: CommandProcessException) {
                actor.onProcessException(this@BungeeCommandWrapper.command,label,ex)
                return@withContext
            }

        }

    }

}