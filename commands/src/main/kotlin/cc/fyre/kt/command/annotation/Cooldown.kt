package cc.fyre.kt.command.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cooldown(
    val cooldown: Long,
)