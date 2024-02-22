package cc.fyre.kt.commands.velocity.converter

import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import com.velocitypowered.api.command.CommandSource

interface BungeeAnnotationConverter<out B: @UnsafeVariance Annotation?,out C: @UnsafeVariance Any?> : AnnotationConverter<CommandSource,B,C>
