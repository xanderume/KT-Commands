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

    override suspend fun preTransform(actor: CommandSender,source: String,annotation: VisibleToPlayer): Boolean {
        return true
    }

    override suspend fun postTransform(actor: CommandSender,source: String,value: Player,annotation: VisibleToPlayer): Player? {

        if (actor !is Player) {
            return value
        }

        if (actor.canSee(actor)) {
            return value
        }

        if (annotation.bypassPermission.isEmpty() && !actor.isOp) {
            return null
        }

        if (!actor.hasPermission(annotation.bypassPermission)) {
            return null
        }

        return value
    }

    override val valueType: KClass<*> = Player::class
    override val annotationType: KClass<*> = VisibleToPlayer::class

}