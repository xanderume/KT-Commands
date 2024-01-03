package cc.fyre.kt.command

import cc.fyre.kt.command.argument.Argument
import cc.fyre.kt.command.exception.CommandProcessException

abstract class CommandActor<out T: Any,out C: Command>(val value: T) {

    abstract fun hasPermission(argument: Argument):Boolean
    abstract fun hasPermission(command: @UnsafeVariance C):Boolean

    abstract fun getLastUsage(command: @UnsafeVariance C):Long
    abstract fun setLastUsage(command: @UnsafeVariance C,time: Long)

    abstract fun onProcessException(command: @UnsafeVariance C,label: String,exception: CommandProcessException)

}