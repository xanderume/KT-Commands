package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.CommandScope
import cc.fyre.kt.command.EmptyMutableList
import cc.fyre.kt.command.argument.flag.Flag
import cc.fyre.kt.command.argument.parameter.Parameter
import cc.fyre.kt.command.exception.CommandProcessException
import com.github.shynixn.mccoroutine.bukkit.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.math.min

/* Simple wrapper class so the compiler doesn't yell at us for having the same JVM signature */
class BukkitCommandWrapper(val command: BukkitCommand) : Command(command.name,command.description ?: "","",command.aliases.toList()), PluginIdentifiableCommand {

    val hidden: Boolean
        get() = this.command.hidden

    init {
        this.setUsage(buildUsage())
    }

    override fun getLabel(): String {
        return this.command.label
    }

    override fun getPlugin(): Plugin {
        return this.command.plugin
    }

    override fun execute(sender: CommandSender, commandLabel: String,args: Array<out String>): Boolean {

        if (this.command.consoleOnly && sender !is ConsoleCommandSender) {
            Audience.sender(sender).sendMessage(ConsoleOnlyMessage)
            return false
        } else if (!this.command.executableByConsole && sender !is Player) {
            Audience.sender(sender).sendMessage(PlayerOnlyMessage)
            return false
        }

        this.plugin.launch(context = CommandScope.coroutineContext) {

            val actor = BukkitCommandPlugin.adapter.createActor(sender)

            try {
                this@BukkitCommandWrapper.command.execute(actor,commandLabel,args)
            } catch (ex: CommandProcessException) {
                actor.onProcessException(this@BukkitCommandWrapper.command,commandLabel,ex)
                return@launch
            }

        }

        return true
    }


    override fun tabComplete(sender: CommandSender,alias: String,args: Array<out String>): MutableList<String> {
        return this.tabComplete(sender,alias,args,null)
    }

    override fun tabComplete(sender: CommandSender,alias: String,args: Array<out String>,location: Location?): MutableList<String> {

        val actor = BukkitCommandPlugin.adapter.createActor(sender)

        if (!actor.hasPermission(this.command)) {
            return EmptyMutableList
        }

        val arg = args.firstOrNull()
        val argLowercase = arg?.lowercase()

        val child = if (argLowercase == null) null else this.command.children[argLowercase]

        if (child != null && actor.hasPermission(child)) {
            return child.wrapper.tabComplete(sender,args[0],args.copyOfRange(1,args.size))
        }

        val function = this.command.function

        if (function == null) {

            if (this.command.children.isEmpty()) {
                return EmptyMutableList
            }

            if (arg != null) {
                return this.command.children.filter{actor.hasPermission(it) && it.name.startsWith(arg,true)}.map{it.name}.toMutableList()
            }

            return this.command.children.filter{actor.hasPermission(it)}.map{it.name}.toMutableList()
        }

        if (args.isEmpty()) {
            return EmptyMutableList
        }

        val argument = if (function.parameters.isEmpty()) {
            null
        } else {
            function.parameters[min(function.parameters.lastIndex,args.lastIndex)]
        }

        val completions = mutableListOf<String>()

        if (this.command.children.isNotEmpty()) {

            if (arg != null) {
                completions.addAll(this.command.children.filter{actor.hasPermission(it) && it.name.startsWith(arg,true)}.map{it.name})
            } else {
                completions.addAll(this.command.children.filter{actor.hasPermission(it)}.map{it.name})
            }

        }

        if (argument != null) {

            var converter = argument.tabCompleter ?: BukkitCommandMap[argument.type]

            if (converter == null && this.command.children.isEmpty()) {
                converter = BukkitCommandMap.defaultTabCompleter
            }

            if (converter != null) {

                var tabCompletion = converter.tabComplete(sender,arg ?: "",args,argument.annotations)

                if (tabCompletion == null) {
                    tabCompletion = BukkitCommandMap.defaultTabCompleter.tabComplete(sender,arg ?: "",args,argument.annotations)
                }

                if (tabCompletion != null) {
                    completions.addAll(tabCompletion)
                }

            }

        }

        return completions
    }

    private fun buildUsage() = buildString{
        append(this@BukkitCommandWrapper.command.name)

        if (this@BukkitCommandWrapper.command.function == null) {
            return@buildString
        }

        for (argument in this@BukkitCommandWrapper.command.function!!.arguments) {

            if (argument.permission != null) {
                continue
            }

            when (argument) {
                is Flag -> {
                    append("(")
                    append("-${argument.flag}") // TODO AQUA?
                    append(")")
                }
                is Parameter -> {
                    if (argument.optional || argument.nullable) {
                        append("[")
                    } else {
                        append("<")
                    }

                    append(argument.name)

                    if (argument.vararg) {
                        append("") // TODO
                    } else if (argument.wildcard) {
                        append("...")
                    }

                    if (argument.optional || argument.nullable) {
                        append("]")
                    } else {
                        append(">")
                    }


                }
            }
        }

    }
}