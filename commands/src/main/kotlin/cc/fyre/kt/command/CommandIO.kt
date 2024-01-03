package cc.fyre.kt.command

import cc.fyre.kt.command.thread.ThreadFactoryBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

val CommandDispatcher = Executors.newFixedThreadPool(2,ThreadFactoryBuilder()
    .setDaemon(true)
    .setNameFormat("Command-Coroutine-%s")
    .build()
).asCoroutineDispatcher()

object CommandScope : CoroutineScope {

    override val coroutineContext: CoroutineContext = CommandDispatcher + SupervisorJob()

}
