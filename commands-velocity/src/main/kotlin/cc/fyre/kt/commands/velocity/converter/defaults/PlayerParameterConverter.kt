package cc.fyre.kt.commands.velocity.converter.defaults

import cc.fyre.kt.commands.velocity.InvalidPlayerMessage
import cc.fyre.kt.commands.velocity.ProxyServer
import cc.fyre.kt.commands.velocity.converter.BungeeParameterConverter
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import kotlin.reflect.KClass

object PlayerParameterConverter : BungeeParameterConverter<Player> {

    override val type: KClass<out Player> = Player::class

    override suspend fun convert(actor: CommandSource,source: String,annotations: Array<out Annotation>): Player? {
        return ProxyServer.getPlayer(source).orElse(null)
    }

    override suspend fun handleException(actor: CommandSource,source: String,exception: Exception?) {
        when (exception) {
            null -> { actor.sendMessage(InvalidPlayerMessage.invoke(source)) }
        }
    }

    override fun tabComplete(sender: CommandSource,source: String,args: Array<out String>,annotations: Array<out Annotation>): MutableList<String> {
        return ProxyServer.allPlayers.filter{it.username.startsWith(source,true)}.map{it.username}.toMutableList()
    }

}