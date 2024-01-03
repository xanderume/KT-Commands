package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.*
import cc.fyre.kt.command.processor.CommandProcessor
import cc.fyre.kt.command.processor.impl.DefaultCommandProcessor
import cc.fyre.kt.commands.bukkit.annotation.Console
import cc.fyre.kt.commands.bukkit.annotation.Hidden
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.full.primaryConstructor

class BukkitCommand(
    override val id: Int,
    override val name: String,
    override val aliases: Array<String>,
    override val permission: String?,
    override var description: String?,
    override val cooldown: Long?,
    override val instance: Any,
    override val commandMap: CommandMap<*>,
    val plugin: JavaPlugin,
): Command {

    val wrapper = BukkitCommandWrapper(this)

    override var helper: CommandHelper<*>? = null
    override var function: CommandFunction? = null
    override var processor: CommandProcessor? = null

    private var _label: String? = null

    override val children = CommandChildMap<BukkitCommand>()

    override val label: String
        get() = this._label ?: this.name

    var hidden: Boolean = false
    var consoleOnly: Boolean = false
    var executableByConsole = this.function?.senderParameter?.type?.classifier != Player::class

    constructor(id: Int,loader: CommandLoader,commandMap: CommandMap<BukkitCommand> = BukkitCommandMap):this(
        id,
        loader.alias.aliases[0],
        loader.alias.aliases.copyOfRange(1,loader.alias.aliases.size).toList().toTypedArray(),
        loader.permission?.permission,
        loader.description?.description,
        loader.cooldown?.cooldown,
        loader.instance,
        commandMap,
        JavaPlugin.getProvidingPlugin(loader.instance::class.java)
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

    fun setLabel(label: String):Boolean {

        if (this.wrapper.setLabel(label)) {
            this._label = label
            return true
        }

        return false
    }

    @JvmName(name = "setDescription1()")
    fun setDescription(description: String) {
        this.description = description
        this.wrapper.setDescription(description)
    }

}