package cc.fyre.kt.commands.bukkit.converter

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import org.bukkit.command.CommandSender

interface BukkitParameterConverter<T: Any> : ParameterConverter<T, CommandSender>