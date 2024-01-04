import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import java.util.*
import kotlin.reflect.KClass

annotation class Nullable
class NullableAnnotationConverter : AnnotationConverter<Any, Nullable, UUID?> {

    override suspend fun preTransform(actor: Any, source: String, annotation: Nullable): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun postTransform(actor: Any, source: String, value: UUID?, annotation: Nullable): UUID? {
        TODO("Not yet implemented")
    }

    override val valueType: KClass<*> = UUID::class
    override val annotationType: KClass<*> = Nullable::class

}