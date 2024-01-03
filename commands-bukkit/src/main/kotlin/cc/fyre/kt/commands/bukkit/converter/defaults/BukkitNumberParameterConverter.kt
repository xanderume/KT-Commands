package cc.fyre.kt.commands.bukkit.converter.defaults

import cc.fyre.kt.command.argument.parameter.defaults.NumberParameterConverter
import cc.fyre.kt.commands.bukkit.Audience
import cc.fyre.kt.commands.bukkit.InvalidNumberMessage
import org.bukkit.command.CommandSender

object BukkitNumberParameterConverter : NumberParameterConverter<CommandSender>() {

    override suspend fun handleException(actor: CommandSender,source: String,exception: Exception?) {
        when (exception) {
            null -> { Audience.sender(actor).sendMessage(InvalidNumberMessage.invoke(source)) }
        }
    }

}