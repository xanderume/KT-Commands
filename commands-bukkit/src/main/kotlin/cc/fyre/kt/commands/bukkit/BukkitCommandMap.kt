package cc.fyre.kt.commands.bukkit

import cc.fyre.kt.command.*
import cc.fyre.kt.command.argument.parameter.ParameterConverter
import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import cc.fyre.kt.command.exception.CommandLoadException
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

object BukkitCommandMap : CommandMap<BukkitCommand> {

    private val map: SimpleCommandMap = CommandMapProperty.getter.call(Bukkit.getServer())

    private val commands = arrayListOf<BukkitCommand>()
    private val commandsBySource = hashMapOf<KClass<*>, BukkitCommand>()
    private val knownCommands: MutableMap<String,org.bukkit.command.Command> = CommandMapKnownCommandsProperty.getter.call(this.map)

    private val parameterConverters = hashSetOf<ParameterConverter<*,*>>()
    private val parameterConvertersBySource = hashMapOf<KClass<*>,ParameterConverter<*,*>>()

    private val annotationConverters = hashSetOf<AnnotationConverter<*, *, *>>()
    private val annotationConvertersBySource = hashMapOf<KClass<*>,HashMap<KClass<*>,AnnotationConverter<*,*,*>>>()

    override val defaultTabCompleter: CommandTabCompleter<*> = BukkitCommandTabCompleter

    fun getKnownCommands():Map<String,org.bukkit.command.Command> {
        return this.knownCommands
    }

    override fun get(command: String): BukkitCommand? {
        return (this.knownCommands[command.lowercase()] as? BukkitCommandWrapper)?.command
    }

    override fun get(source: Any): BukkitCommand? {
        return this.commandsBySource[source::class]
    }

    override fun get(source: KClass<*>): ParameterConverter<*,*>? {
        return this.parameterConvertersBySource[source]
    }

    override fun get(annotation: KClass<out Annotation>,type: KClass<*>): AnnotationConverter<*,*,*>? {

        val map = this.annotationConvertersBySource[annotation]

        if (map.isNullOrEmpty()) {
            return null
        }

        return map[type]
    }

    override fun register(command: Any, parent: Command?): Command {

        val loader = loadCommand(command)
        val result = BukkitCommand(this.commands.size,loader)

        if (loader.function == null && loader.helper == null) {
            throw CommandLoadException(command,"Command must either have a ${CommandExecutor::class.simpleName} function or sub commands!")
        }

        if (parent != null) {
            parent.children.add(result)
            result.setLabel("${parent.label} ${result.name}")
        } else if (loader.parent != null) {

            val source = this.commandsBySource[loader.parent!!.parent]
                ?: throw CommandLoadException(command,"Parent ${loader.parent!!.parent.simpleName} has not been registered!")

            return register(command,source)
        }

        this.commands.add(result)
        this.commandsBySource[loader.clazz] = result

        if (result.children.isNotEmpty()) {

            if (result.function == null && result.description?.isEmpty() == true) {
                result.setDescription("View all available ${result.name} commands")
            }

        }

        if (parent == null) {
            this.map.register(result.plugin.name,result.wrapper)
            Bukkit.getServer().helpMap.addTopic(BukkitCommandTopic(result))
        }


        return result
    }

    override fun register(converter: ParameterConverter<*,*>,override: Boolean) {

        if (!override && this.parameterConvertersBySource.containsKey(converter.type)) {
            error("A converter for ${converter.type.simpleName!!} has already been registered!")
        }

        this.parameterConverters.add(converter)
        this.parameterConvertersBySource[converter.type] = converter
    }

    override fun register(converter: AnnotationConverter<*,*,*>,vararg subTypes: KClass<*>) {

        val map = this.annotationConvertersBySource.getOrPut(converter.annotationType) { hashMapOf() }

        for (type in subTypes) {
            map[type] = converter
        }

        map[converter.valueType] = converter

        this.annotationConverters.add(converter)
    }

    override fun iterator(): Iterator<BukkitCommand> {
        return this.commands.iterator()
    }

}