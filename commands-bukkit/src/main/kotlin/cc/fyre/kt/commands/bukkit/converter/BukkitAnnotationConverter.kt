package cc.fyre.kt.commands.bukkit.converter

import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import org.bukkit.command.CommandSender

interface BukkitAnnotationConverter<out B: @UnsafeVariance Annotation?,out C: @UnsafeVariance Any?> : AnnotationConverter<CommandSender, B, C>