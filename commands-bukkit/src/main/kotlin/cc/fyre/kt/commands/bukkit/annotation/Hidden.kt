package cc.fyre.kt.commands.bukkit.annotation

import cc.fyre.kt.command.CommandAnnotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@CommandAnnotation
annotation class Hidden
