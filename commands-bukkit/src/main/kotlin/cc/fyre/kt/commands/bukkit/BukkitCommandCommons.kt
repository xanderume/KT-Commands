package cc.fyre.kt.commands.bukkit

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

val Audience = BukkitAudiences.create(BukkitCommandPlugin.instance)

internal const val LastCommandUsagePrefix = "LastCommandUsage"

internal val CommandMapProperty: KProperty<SimpleCommandMap> = Bukkit.getServer()::class.declaredMemberProperties.first{it.name == "commandMap"}
    .apply{this.isAccessible = true} as KProperty<SimpleCommandMap>

internal val CommandMapKnownCommandsProperty: KProperty<MutableMap<String,Command>> = SimpleCommandMap::class.declaredMemberProperties.first{it.name == "knownCommands"}
    .apply{this.isAccessible = true} as KProperty<MutableMap<String,Command>>