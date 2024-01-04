import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import java.util.UUID
import kotlin.reflect.KClass

annotation class NonNull
class NonNullAnnotationConverter : AnnotationConverter<Any,NonNull,UUID> {

    override suspend fun preTransform(actor: Any, source: String, annotation: NonNull): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun postTransform(actor: Any, source: String, value: UUID, annotation: NonNull): UUID? {
        TODO("Not yet implemented")
    }

    override val valueType: KClass<*> = UUID::class
    override val annotationType: KClass<*> = NonNull::class

}