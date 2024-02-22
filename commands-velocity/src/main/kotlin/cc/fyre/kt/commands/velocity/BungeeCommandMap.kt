package cc.fyre.kt.commands.velocity

import cc.fyre.kt.command.*
import cc.fyre.kt.command.argument.parameter.ParameterConverter
import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import cc.fyre.kt.command.exception.CommandLoadException
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import kotlin.reflect.KClass

object BungeeCommandMap : CommandMap<BungeeCommand> {

    private val commands = arrayListOf<BungeeCommand>()
    private val commandsByName = hashMapOf<String,BungeeCommand>()
    private val commandsBySource = hashMapOf<KClass<*>, BungeeCommand>()

    private val parameterConverters = hashSetOf<ParameterConverter<*,*>>()
    private val parameterConvertersBySource = hashMapOf<KClass<*>,ParameterConverter<*,*>>()

    private val annotationConverters = hashSetOf<AnnotationConverter<*, *, *>>()
    private val annotationConvertersBySource = hashMapOf<KClass<*>,HashMap<KClass<*>,AnnotationConverter<*,*,*>>>()

    override val defaultTabCompleter: CommandTabCompleter<*> = BungeeCommandTabCompleter

    override fun get(command: String): BungeeCommand? {
        return this.commandsByName[command.lowercase()]
    }

    override fun get(source: Any): BungeeCommand? {
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
        val result = BungeeCommand(this.commands.size,loader)

        if (loader.function == null && loader.helper == null) {
            throw CommandLoadException(command,"Command must either have a ${CommandExecutor::class.simpleName} function or a helper!")
        }

        if (parent != null) {
            parent.children.add(result)
            result.label = ("${parent.label} ${result.name}")
        } else if (loader.parent != null) {

            val source = this.commandsBySource[loader.parent!!.parent]
                ?: throw CommandLoadException(command,"Parent ${loader.parent!!.parent.simpleName} has not been registered!")

            return register(command,source)
        }

        this.commands.add(result)
        this.commandsBySource[loader.clazz] = result

        if (result.children.isNotEmpty()) {

            if (result.function == null && result.description?.isEmpty() == true) {
                result.description = "View all available ${result.name} commands"
            }

        }

        val commandManager = ProxyServer.commandManager

        if (parent == null) {
            commandManager.registerSuspend(commandManager.metaBuilder(result.name)
                .aliases(*result.aliases)
                .aliases().build(),result.wrapper,BungeeCommandPlugin.instance) // TODO: plugin instance
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

    override fun iterator(): Iterator<BungeeCommand> {
        return this.commands.iterator()
    }
    
}