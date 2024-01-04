import cc.fyre.kt.command.argument.parameter.annotation.AnnotationConverter
import kotlin.reflect.full.starProjectedType
import kotlin.test.Test

class Testing {

    @Test
    fun testNullable() {

        val nonNull = NonNullAnnotationConverter()
        val nullable = NullableAnnotationConverter()

        println(AnnotationConverter::class.typeParameters.joinToString{"${it.starProjectedType.isMarkedNullable}"})
        println(nonNull::class.supertypes[0].arguments.joinToString{"${it.type!!.isMarkedNullable}"})
        println(nullable::class.supertypes[0].arguments.joinToString{"${it.type!!.isMarkedNullable}"})
    }

}