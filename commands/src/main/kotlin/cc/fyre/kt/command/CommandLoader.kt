package cc.fyre.kt.command

import cc.fyre.kt.command.annotation.*
import cc.fyre.kt.command.exception.CommandLoadException
import kotlin.jvm.Throws
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

class CommandLoader(
    val clazz: KClass<*>,
    val instance: Any,
    val alias: Alias,
    val cooldown: Cooldown?,
    val permission: Permission?,
    val description: Description?,
    val helper: Helper?,
    val parent: Parent?,
    val annotations: Array<Annotation>,
    val function: KFunction<*>?,
)

@Throws(CommandLoadException::class)
fun loadCommand(command: Any):CommandLoader {

    val clazz: KClass<*>
    var instance: Any?

    if (command is KClass<*>) {
        clazz = command
        instance = command.objectInstance ?: command::class.companionObjectInstance

        if (instance == null) {
            instance = command::class.primaryConstructor?.call() ?: throw CommandLoadException(command,"Command be an object, have an companion object or have an empty constructor!")
        }

    } else {
        clazz = command::class
        instance = command
    }

    val alias = clazz.findAnnotation<Alias>()

    if (alias == null || alias.aliases.isEmpty()) {
        throw CommandLoadException(clazz,"Empty aliases")
    }

    var cooldown: Cooldown? = null
    var permission: Permission? = null
    var description: Description? = null
    var helper: Helper? = null
    var parent: Parent? = null

    val function = clazz.functions.firstOrNull{it.hasAnnotation<CommandExecutor>()}
    val annotations = arrayListOf<Annotation>()

    for (annotation in clazz.annotations) {

        when (annotation) {
            is Cooldown -> cooldown = annotation
            is Permission -> permission = annotation
            is Description -> description = annotation
            is Helper -> helper = annotation
            is Parent -> parent = annotation
            else -> annotations.add(annotation)
        }

    }

    return CommandLoader(
        clazz,
        instance,
        alias,
        cooldown,
        permission,
        description,
        helper,
        parent,
        annotations.toTypedArray(),
        function
    )
}

