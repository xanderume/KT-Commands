package cc.fyre.kt.command.processor

import cc.fyre.kt.command.CommandActor
import cc.fyre.kt.command.CommandFunction
import cc.fyre.kt.command.CommandMap
import kotlin.reflect.KParameter

interface CommandProcessor {

    val function: CommandFunction
    val commandMap: CommandMap<*>

    suspend fun process(actor: CommandActor<*,*>, label: String, arguments: Array<out String>):suspend () -> MutableMap<KParameter,Any?>

}