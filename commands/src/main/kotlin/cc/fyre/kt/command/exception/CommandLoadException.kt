package cc.fyre.kt.command.exception


class CommandLoadException(private val command: Any,message: String) : RuntimeException() {

    override val message: String = "cannot load ${this.command::class.simpleName!!}: $message"

}