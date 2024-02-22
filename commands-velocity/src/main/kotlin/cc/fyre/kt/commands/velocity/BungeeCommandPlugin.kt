package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.argument.parameter.defaults.StringParameterConverter
import cc.fyre.kt.commands.velocity.command.KTCommand
import cc.fyre.kt.commands.velocity.converter.defaults.BungeeDurationParameterConverter
import cc.fyre.kt.commands.velocity.converter.defaults.BungeeNumberParameterConverter
import cc.fyre.kt.commands.velocity.converter.defaults.PlayerParameterConverter
import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer

@Plugin(
    id = "kt-commands",
    url = "https://fyre.cc",
    name = "KTCommands",
    version = "1.0.0",
    authors = ["Fyre Services"]
)
class BungeeCommandPlugin @Inject constructor(val suspendingPluginContainer: SuspendingPluginContainer) {

    init {
        instance = this
        adapter = BungeeCommandAdapter
        suspendingPluginContainer.initialize(this)

        BungeeCommandMap.register(KTCommand)
        BungeeCommandMap.register(StringParameterConverter)
        BungeeCommandMap.register(PlayerParameterConverter)
        BungeeCommandMap.register(BungeeDurationParameterConverter)
        BungeeCommandMap.register(BungeeNumberParameterConverter)
    }

    fun getProxyServer():ProxyServer {
        return this.suspendingPluginContainer.server
    }

    companion object {

        lateinit var adapter: BungeeCommandAdapter
        lateinit var instance: BungeeCommandPlugin

    }

}