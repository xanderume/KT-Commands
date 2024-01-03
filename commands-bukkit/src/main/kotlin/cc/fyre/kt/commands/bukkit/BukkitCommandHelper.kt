package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.Command
import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.CommandHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import kotlin.math.min

open class BukkitCommandHelper(override val command: Command) : CommandHelper<BukkitCommandActor> {

    private val line = Component.text()
        .append(Component.text(" ".repeat(32)))
        .color(NamedTextColor.BLUE)
        .decorate(TextDecoration.STRIKETHROUGH)
        .build()

    override fun sendMessage(actor: BukkitCommandActor,label: String,page: Int) {
        var value = page

        if (value > this.pages) {
            value = this.pages
        }

        this.preSendMessage(actor,label,page)

        val commands = if (this.commandsPerPage <= 0) {
            this.command.children
        } else {

            if (value <= 1) {
                this.command.children.subList(0,this.commandsPerPage - 1)
            } else {
                val start = this.commandsPerPage * (value + 1)
                this.command.children.subList(start,min(this.command.children.lastIndex,start + (this.commandsPerPage - 1)))
            }

        }

        for (child in commands) {
            child as BukkitCommand

            val permission = child.permission

            if (permission != null && !actor.hasPermission(child)) {
                continue
            }

            this.sendCommandUsage(actor,label,child)
        }

        this.postSendMessage(actor,label,page)
    }

    override fun preSendMessage(actor: BukkitCommandActor,label: String,page: Int) {
        Audience.sender(actor.sender).sendMessage(this.line)
    }

    override fun postSendMessage(actor: BukkitCommandActor,label: String,page: Int) {
        Audience.sender(actor.sender).sendMessage(this.line)
    }

    override fun sendCommandUsage(actor: BukkitCommandActor,label: String,command: Command) {

        val component = Component.text()

        component.append(Component.text("/$label").color(NamedTextColor.RED))
        component.appendSpace()
        component.append(Component.text(command.name).color(NamedTextColor.RED))
        component.appendSpace()
        component.append(BukkitCommandPlugin.adapter.createUsage(actor,command))

        val description = command.description

        if (!description.isNullOrEmpty()) {
            component.hoverEvent(HoverEvent.showText(Component.text(description).color(NamedTextColor.GRAY)))
        }

        Audience.sender(actor.sender).sendMessage(component.build())
    }

}