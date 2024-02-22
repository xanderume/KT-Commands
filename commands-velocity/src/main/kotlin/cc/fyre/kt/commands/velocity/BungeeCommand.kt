package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.*
import cc.fyre.kt.command.processor.CommandProcessor
import cc.fyre.kt.command.processor.impl.DefaultCommandProcessor
import cc.fyre.kt.commands.velocity.annotation.Console
import cc.fyre.kt.commands.velocity.annotation.Hidden
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.Player
import kotlin.reflect.full.primaryConstructor

class BungeeCommand(
    override val id: Int,
    override val name: String,
    override val aliases: Array<String>,
    override val permission: String?,
    override var description: String?,
    override val cooldown: Long?,
    override val instance: Any,
    override val commandMap: CommandMap<*>,
    val plugin: PluginContainer,
): Command {

    val wrapper = BungeeCommandWrapper(this)

    override var helper: CommandHelper<*>? = null
    override var function: CommandFunction? = null
    override var processor: CommandProcessor? = null

    override val children = CommandChildMap<BungeeCommand>()

    override var label: String = this.name

    var hidden: Boolean = false
    var consoleOnly: Boolean = false
    var executableByConsole = this.function?.senderParameter?.type?.classifier != Player::class

    constructor(id: Int, loader: CommandLoader, commandMap: CommandMap<BungeeCommand> = BungeeCommandMap):this(
        id,
        loader.alias.aliases[0],
        loader.alias.aliases.copyOfRange(1,loader.alias.aliases.size).toList().toTypedArray(),
        loader.permission?.permission,
        loader.description?.description,
        loader.cooldown?.cooldown,
        loader.instance,
        commandMap,
        BungeeCommandPlugin.instance.suspendingPluginContainer.pluginContainer,
//        loader.clazz.pluginContainer
    ) {

        if (loader.function != null) {
            this.function = CommandFunction(this,loader.function!!)
            this.processor = DefaultCommandProcessor(this.function!!,this.commandMap)
        }

        if (loader.helper != null) {
            this.helper = loader.helper!!.helper.primaryConstructor!!.call(this)
        }

        loader.annotations.forEach{
            when (it) {
                is Hidden -> this.hidden = true
                is Console -> this.consoleOnly = true
            }
        }
    }

}