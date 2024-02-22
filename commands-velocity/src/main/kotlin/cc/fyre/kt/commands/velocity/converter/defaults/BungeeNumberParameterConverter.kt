package cc.fyre.kt.commands.velocity.converter.defaults

import cc.fyre.kt.command.argument.parameter.defaults.NumberParameterConverter
import cc.fyre.kt.commands.velocity.InvalidNumberMessage
import com.velocitypowered.api.command.CommandSource

object BungeeNumberParameterConverter : NumberParameterConverter<CommandSource>() {

    override suspend fun handleException(actor: CommandSource,source: String,exception: Exception?) {
        when (exception) {
            null -> { actor.sendMessage(InvalidNumberMessage.invoke(source)) }
        }
    }

}