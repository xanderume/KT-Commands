package cc.fyre.kt.command

import kotlin.math.ceil

interface CommandHelper<out A: CommandActor<*,*>>  {

    val command: Command

    val pages: Int
        get() = if (this.commandsPerPage == -1) {
            1
        } else {
            ceil(this.commandsPerPage.toDouble() / this.command.children.size.toDouble()).toInt()
        }

    val commandsPerPage: Int
        get() = -1

    fun sendMessage(actor: @UnsafeVariance A,label: String,page: Int)
    fun preSendMessage(actor: @UnsafeVariance A,label: String,page: Int)
    fun postSendMessage(actor: @UnsafeVariance A,label: String,page: Int)
    fun sendCommandUsage(actor: @UnsafeVariance A,label: String,command: Command)

}