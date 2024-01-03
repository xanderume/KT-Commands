package cc.fyre.kt.command.argument.parameter

import cc.fyre.kt.command.CommandTabCompleter
import kotlin.reflect.KClass

interface ParameterConverter<T: Any,out A: Any> : CommandTabCompleter<A> {

    val type: KClass<out T>

    suspend fun convert(actor: @UnsafeVariance A,source: String,annotations: Array<out Annotation>):T?
    suspend fun handleException(actor: @UnsafeVariance A,source: String,exception: Exception?)

    override fun tabComplete(sender: @UnsafeVariance A,source: String,args: Array<out String>, annotations: Array<out Annotation>): MutableList<String>? = null

}