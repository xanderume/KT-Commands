package cc.fyre.kt.command

interface CommandTabCompleter<out A: @UnsafeVariance Any> {

    fun tabComplete(sender: @UnsafeVariance A,source: String,args: Array<out String>,annotations: Array<out Annotation>):MutableList<String>?

}