package cc.fyre.kt.command.argument.parameter.annotation

import kotlin.reflect.KClass

interface AnnotationConverter<out A: @UnsafeVariance Any,out B: @UnsafeVariance Annotation?,out C: @UnsafeVariance Any?> {

    fun preTransform(actor: @UnsafeVariance A,source: String,annotation: @UnsafeVariance B):Boolean
    fun postTransform(actor: @UnsafeVariance A,source: @UnsafeVariance C,annotation: @UnsafeVariance B):Boolean

    fun onTypeRegister(annotation: @UnsafeVariance B) {}

    val valueType: KClass<*>
    val annotationType: KClass<*>

}