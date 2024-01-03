package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.argument.parameter.defaults.StringParameterConverter
import cc.fyre.kt.commands.bukkit.command.KTCommand
import cc.fyre.kt.commands.bukkit.converter.defaults.BukkitDurationParameterConverter
import cc.fyre.kt.commands.bukkit.converter.defaults.BukkitNumberParameterConverter
import cc.fyre.kt.commands.bukkit.converter.defaults.PlayerParameterConverter
import cc.fyre.kt.commands.bukkit.converter.defaults.annotation.PlayerVisibilityConverter
import org.bukkit.plugin.java.JavaPlugin

class BukkitCommandPlugin : JavaPlugin() {

    override fun onEnable() {
        adapter = BukkitCommandAdapter
        instance = this

        BukkitCommandMap.register(KTCommand)
        BukkitCommandMap.register(StringParameterConverter)
        BukkitCommandMap.register(PlayerParameterConverter)
        BukkitCommandMap.register(BukkitDurationParameterConverter)
        BukkitCommandMap.register(BukkitNumberParameterConverter)
        BukkitCommandMap.register(PlayerVisibilityConverter)
    }

    override fun onDisable() {

    }

    companion object {

        lateinit var adapter: BukkitCommandAdapter
        lateinit var instance: BukkitCommandPlugin

    }

}