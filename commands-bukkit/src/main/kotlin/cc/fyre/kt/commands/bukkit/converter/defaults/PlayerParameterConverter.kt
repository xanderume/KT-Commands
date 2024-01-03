package cc.fyre.kt.commands.bukkit.converter.defaults

import cc.fyre.kt.commands.bukkit.Audience
import cc.fyre.kt.commands.bukkit.InvalidPlayerMessage
import cc.fyre.kt.commands.bukkit.converter.BukkitParameterConverter
import cc.fyre.kt.commands.bukkit.converter.defaults.annotation.VisibleToPlayer
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

object PlayerParameterConverter : BukkitParameterConverter<Player> {

    override val type: KClass<out Player> = Player::class

    override suspend fun convert(actor: CommandSender,source: String,annotations: Array<out Annotation>): Player? {
        return Bukkit.getServer().getPlayer(source)
    }

    override suspend fun handleException(actor: CommandSender,source: String,exception: Exception?) {
        when (exception) {
            null -> { Audience.sender(actor).sendMessage(InvalidPlayerMessage.invoke(source)) }
        }
    }

    override fun tabComplete(sender: CommandSender,source: String,args: Array<out String>,annotations: Array<out Annotation>): MutableList<String> {
        val visibleToPlayer = annotations.filterIsInstance<VisibleToPlayer>().firstOrNull()
        return Bukkit.getServer().onlinePlayers.filter{

            if (sender is Player && visibleToPlayer != null) {

                if (visibleToPlayer.bypassPermission.isEmpty() && !sender.canSee(it)) {
                    return@filter false
                }

            }

            return@filter it.name.startsWith(source,true)
        }.map{it.name}.toMutableList()
    }

}