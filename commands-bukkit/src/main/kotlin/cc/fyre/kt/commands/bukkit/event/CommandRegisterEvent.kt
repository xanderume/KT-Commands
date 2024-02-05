package cc.fyre.kt.commands.bukkit.event

import cc.fyre.kt.command.CommandLoader
import cc.fyre.kt.commands.bukkit.BukkitCommand
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CommandRegisterEvent(
    val loader: CommandLoader,
    val command: BukkitCommand
) : Event(),Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {

        @JvmStatic val handlerList = HandlerList()

    }

}