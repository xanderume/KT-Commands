# Kotlin Command Library

A Kotlin library for creating an annotation-based command system, leveraging Kotlin's coroutines and various Kotlin features. The current implementation includes support for Bukkit, with plans to add support for Velocity and BungeeCord.

## Features

- Annotation-based command system
- Utilizes Kotlin coroutines (Async)
- Platform actor support (Bukkit implemented, Velocity and BungeeCord planned)
- Parameter converter interface for custom conversions
- Custom annotation converters
- Nullable and default values for parameters
- Wildcards
- Tab completion support

## Roadmap

- [x] Bukkit support
- [ ] Velocity support (planned)
- [ ] BungeeCord support (planned)
- [ ] Additional features (planned)”
- [ ] Vararg support (if possible) (WIP)

## Support

For help or support, contact me on discord **xander8366** or create an issue [here](https://github.com/xanderume/KT-Commands/issues).

## Getting Started

### Creating a Command
```kotlin
@Alias(aliases = ["test"])
@Description(description = "Test!")
object TestCommand {

    @CommandExecutor
    fun execute(sender: CommandSender) {
        sender.sendMessage("Test1")
    }

}
```
### Setting a Permission
```kotlin
@Permission(permission = "command.test")
object TestCommand
```

### Adding a Cooldown
```kotlin
@Cooldown(cooldown = 1000L)
object TestCommand
```
### Making a sub command
#### The command will now be /test test2
```kotlin
@Alias(aliases = ["test2"])
@Parent(parent = TestCommand::class)
@Permission(permission = "command.test2")
object Test2Command
```
### Adding Parameters
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,player: Player,number: Int) {
    sender.sendMessage("Sent $number to ${player.name}")
    player.sendMessage(number.toString())
}
```
### Adding a default value to a Parameter
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,player: Player,number: Number = 3) {
    sender.sendMessage("Sent $number to ${player.name}")
    player.sendMessage(number.toString())
}
```
### Making a Parameter optional
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,player: Player,number: Number? = null) {
    sender.sendMessage("Sent ${number?.toString() ?: "null"} to ${player.name}")
    player.sendMessage(number?.toString() ?: "null")
}
```
### Making a Parameter wildcard
#### Note: Wildcards (@Wildcard) are only valid for the last parameter of the command.
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,@VisibleToPlayer player: Player,@Wildcard text: String) {
    sender.sendMessage("Sent $text to ${player.name}")
    player.sendMessage(text)
}
```
### Adding a custom Annotation
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,@VisibleToPlayer player: Player,@Wildcard text: String) {
    sender.sendMessage("Sent $text to ${player.name}")
    player.sendMessage(text)
}
```

### Adding a Flag (-l)
#### Note: Flags (@Flag) must always be a boolean
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,@Flag(flags = ["l"]) log: Boolean,@VisibleToPlayer player: Player,@Wildcard text: String) {
    sender.sendMessage("Sent $text to ${player.name}")
    player.sendMessage(text)
    
    if (log) {
        println("${sender.name} sent $text to ${player.name}")
    }
}
```
### Adding a permission to a Flag
```kotlin
@CommandExecutor
fun execute(sender: CommandSender,@Permission(permission = "command.test.flag") @Flag(flags = ["l"]) log: Boolean,@VisibleToPlayer player: Player,@Wildcard text: String) {
    sender.sendMessage("Sent $text to ${player.name}")
    player.sendMessage(text)
    
    if (log) {
        println("${sender.name} sent $text to ${player.name}")
    }
}
```
### Running a command on a different coroutine
```kotlin
@CommandExecutor
suspend fun execute(sender: CommandSender) = withContext(Dispatchers.IO) {
    sender.sendMessage("Ran command on ${Thread.currentThread().name}")
}
```
### Creating a Parameter Converter
```kotlin
@CommandExecutor
object PlayerParameterConverter : BukkitParameterConverter<Player> {

    override val type: KClass<out Player> = Player::class

    override suspend fun convert(actor: CommandSender,source: String,annotations: Array<out Annotation>): Player? {
        return Bukkit.getServer().getPlayer(source)
    }

    override suspend fun handleException(actor: CommandSender,source: String,exception: Exception?) {
        when (exception) {
            null -> { Audience.sender(actor).sendMessage(InvalidPlayerMessage.invoke(source)) }
        }
    }

    override fun tabComplete(sender: CommandSender,source: String,args: Array<out String>,annotations: Array<out Annotation>): MutableList<String> {
        return Bukkit.getServer().onlinePlayers.filter{

            if (sender is Player) {

                if (!sender.canSee(it)) {
                    return@filter false
                }

            }

            return@filter it.name.startsWith(source,true)
        }.map{it.name}.toMutableList()
    }

}
```
### Creating a Annotation Converter
```kotlin
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class VisibleToPlayer(
    val bypassPermission: String = ""
)

object PlayerVisibilityConverter : BukkitAnnotationConverter<VisibleToPlayer,Player> {

    override val valueType: KClass<*> = Player::class
    override val annotationType: KClass<*> = VisibleToPlayer::class

    override fun preTransform(actor: CommandSender,source: String,annotation: VisibleToPlayer): Boolean {
        return true
    }

    override fun postTransform(actor: CommandSender,source: Player,annotation: VisibleToPlayer): Boolean {

        if (actor !is Player) {
            return true
        }

        if (actor.canSee(actor)) {
            return true
        }

        if (annotation.bypassPermission.isEmpty()) {
            return false
        }

        return actor.hasPermission(annotation.bypassPermission)
    }

}
```
## License

This project is licensed under the [MIT License](LICENSE).