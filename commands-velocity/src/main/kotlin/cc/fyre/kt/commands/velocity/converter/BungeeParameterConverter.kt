package cc.fyre.kt.commands.velocity.converter

import cc.fyre.kt.command.argument.parameter.ParameterConverter
import com.velocitypowered.api.command.CommandSource

interface BungeeParameterConverter<T: Any> : ParameterConverter<T,CommandSource>