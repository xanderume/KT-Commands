package cc.fyre.kt.command.argument.parameter.annotation

import kotlin.reflect.KClass

interface AnnotationConverter<out A: @UnsafeVariance Any,out B: @UnsafeVariance Annotation?,out C: @UnsafeVariance Any?> {

    fun onTypeRegister(annotation: @UnsafeVariance B) {}

    suspend fun preTransform(actor: @UnsafeVariance A,source: String,annotation: @UnsafeVariance B):Boolean
    suspend fun postTransform(actor: @UnsafeVariance A,source: String,value: @UnsafeVariance C, annotation: @UnsafeVariance B):C?

    val nullable: Boolean
        get() = this::class.supertypes[0].arguments.last().type!!.isMarkedNullable

    val valueType: KClass<*>
    val annotationType: KClass<*>
//
//    suspend fun endWith(value: @UnsafeVariance C?): AnnotationContinuation<@UnsafeVariance C?> {
//        return object : AnnotationContinuation<@UnsafeVariance C?> {
//            override val value: C? = value
//            override val resume: Boolean = false
//        }
//    }
//
//    suspend fun resumeWith(value: @UnsafeVariance C?): AnnotationContinuation<@UnsafeVariance C?> {
//        return object : AnnotationContinuation<@UnsafeVariance C?> {
//            override val value: C? = value
//            override val resume: Boolean = true
//        }
//    }

}