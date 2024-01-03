package cc.fyre.kt.commands.bukkit.converter.defaults.annotation

import cc.fyre.kt.commands.bukkit.converter.BukkitAnnotationConverter
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

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