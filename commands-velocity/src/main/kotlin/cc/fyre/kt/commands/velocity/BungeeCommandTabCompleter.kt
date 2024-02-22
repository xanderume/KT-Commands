package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.CommandTabCompleter
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer

interface BungeeCommandTabCompleter : CommandTabCompleter<CommandSource> {

    companion object : BungeeCommandTabCompleter {

        override fun tabComplete(sender: CommandSource,source: String,args: Array<out String>,annotations: Array<out Annotation>): MutableList<String> {
            return ProxyServer.allPlayers.filter{it.username.startsWith(source,true)}.map{it.username}.toMutableList()
        }

    }

}