package cc.fyre.kt.commands.bukkit

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor

val CooldownMessage = Component.text("Please do not spam this command!")
    .color(NamedTextColor.RED)
val PlayerOnlyMessage = Component.text("This command can only be executed in in-game!")
    .color(NamedTextColor.RED)
val ConsoleOnlyMessage = Component.text("This command can only be executed in console!")
    .color(NamedTextColor.RED)
val NoPermissionMessage = Component.text("No permission.")
    .color(NamedTextColor.RED)
val CommandExecutionFailedMessage = Component.text("An issue occurred processing your command.")
    .color(NamedTextColor.RED)

val InvalidPlayerMessage: (source: String) -> TextComponent = {source ->
    Component.text()
        .append(Component.text("No player with the name"))
        .appendSpace()
        .append(Component.text("\"$source\""))
        .appendSpace()
        .append(Component.text("found."))
        .color(NamedTextColor.RED)
        .build()
}

val InvalidNumberMessage: (source: String) -> TextComponent = {source ->
    Component.text()
        .append(Component.text("\"$source\""))
        .appendSpace()
        .append(Component.text("is not a valid number."))
        .color(NamedTextColor.RED)
        .build()
}

val InvalidDurationMessage: (source: String) -> TextComponent = {source ->
    Component.text()
        .append(Component.text("\"$source\""))
        .appendSpace()
        .append(Component.text("is not a valid duration."))
        .color(NamedTextColor.RED)
        .build()
}
