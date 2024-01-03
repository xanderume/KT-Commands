package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.CommandTabCompleter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface BukkitCommandTabCompleter : CommandTabCompleter<CommandSender> {

    companion object : BukkitCommandTabCompleter {

        override fun tabComplete(sender: CommandSender, source: String, args: Array<out String>, annotations: Array<out Annotation>): MutableList<String> {
            return Bukkit.getServer().onlinePlayers.filter{

                if (sender is Player && !sender.canSee(it)) {
                    return@filter false
                }

                return@filter it.name.startsWith(source,true)
            }.map{it.name}.toMutableList()
        }

    }

}