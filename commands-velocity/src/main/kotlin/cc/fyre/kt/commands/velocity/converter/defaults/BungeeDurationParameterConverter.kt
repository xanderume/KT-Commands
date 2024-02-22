package cc.fyre.kt.commands.velocity.converter.defaults

import cc.fyre.kt.command.argument.parameter.defaults.DurationParameterConverter
import cc.fyre.kt.commands.velocity.InvalidDurationMessage
import com.velocitypowered.api.command.CommandSource

object BungeeDurationParameterConverter : DurationParameterConverter<CommandSource>() {

    override suspend fun handleException(actor: CommandSource,source: String,exception: Exception?) {
        when (exception) {
            null -> { actor.sendMessage(InvalidDurationMessage.invoke(source)) }
        }
    }

}