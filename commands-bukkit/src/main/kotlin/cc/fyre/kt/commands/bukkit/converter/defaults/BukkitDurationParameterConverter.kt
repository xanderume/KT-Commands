package cc.fyre.kt.commands.bukkit.converter.defaults

import cc.fyre.kt.command.argument.parameter.defaults.DurationParameterConverter
import cc.fyre.kt.commands.bukkit.Audience
import cc.fyre.kt.commands.bukkit.InvalidDurationMessage
import org.bukkit.command.CommandSender

object BukkitDurationParameterConverter : DurationParameterConverter<CommandSender>() {

    override suspend fun handleException(actor: CommandSender,source: String,exception: Exception?) {
        when (exception) {
            null -> { Audience.sender(actor).sendMessage(InvalidDurationMessage.invoke(source)) }
        }
    }

}