package cc.fyre.kt.commands.velocity

import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import java.net.URLClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

val ProxyServer: ProxyServer
    get() = BungeeCommandPlugin.instance.getProxyServer()

//val KClass<*>.pluginContainer: PluginContainer
//    get() {
//
//        val loader = this.java.classLoader as URLClassLoader
//
////        println("Loader: ${loader}")
//        println("URLS: ${loader.urLs.joinToString()}")
//        println("Packages: ${loader.definedPackages.joinToString()}")
//        println(PluginClassLoaderLoadClass0.call(loader,this.java.name,true,true))
//
//        error("")
//    }

//private val PluginClassLoader = Class.forName("com.velocitypowered.proxy.plugin.PluginClassLoader").kotlin
//private val PluginClassLoaderLoadClass0 = PluginClassLoader.memberFunctions.first{it.name == "loadClass0"}.apply{this.isAccessible = true}