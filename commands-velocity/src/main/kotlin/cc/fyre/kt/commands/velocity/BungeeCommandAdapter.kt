package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.Command
import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.argument.flag.Flag
import cc.fyre.kt.command.argument.parameter.Parameter
import com.velocitypowered.api.command.CommandSource
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

interface BungeeCommandAdapter {

    fun createActor(sender: CommandSource): CommandActor<CommandSource,BungeeCommand> {
        return BungeeCommandActor(sender)
    }

    fun createUsage(actor: CommandActor<CommandSource,BungeeCommand>, command: Command): Component {

        val function = command.function
        val component = Component.text()

        if (function != null) {

            val array = function.flags.plus(function.parameters)

            for (i in array.indices) {

                val argument = array[i]

                if (!actor.hasPermission(argument)) {
                    continue
                }

                when (argument) {
                    is Flag -> {
                        component.append(Component.text("("))
                        component.append(Component.text("-${argument.flag}").color(NamedTextColor.AQUA))
                        component.append(Component.text(")"))
                        component.color(NamedTextColor.RED)
                    }
                    is Parameter -> {

                        if (argument.optional || argument.nullable) {
                            component.append(Component.text("["))
                        } else {
                            component.append(Component.text("<"))
                        }

                        component.append(Component.text(argument.name))

                        if (argument.vararg) {
                            component.append(Component.text(""))
                        } else if (argument.wildcard) {
                            component.append(Component.text("..."))
                        }

                        if (argument.optional || argument.nullable) {
                            component.append(Component.text("]"))
                        } else {
                            component.append(Component.text(">"))
                        }

                        component.color(NamedTextColor.RED)
                    }
                }

                val description = argument.description

                if (description != null) {
                    component.hoverEvent(HoverEvent.showText(Component.text(description).color(NamedTextColor.GRAY)))
                }

                if (i != array.lastIndex) {
                    component.append(Component.space())
                }

            }

        }

        return component.build()
    }

    companion object : BungeeCommandAdapter


}