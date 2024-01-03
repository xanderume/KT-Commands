package cc.fyre.kt.command.thread

import kotlinx.atomicfu.atomic
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong


class ThreadFactoryBuilder {

    private var nameFormat: String? = null
    private var daemon: Boolean? = null
    private var priority: Int? = null
    private var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    private var backingThreadFactory: ThreadFactory? = null

    fun setNameFormat(nameFormat: String): ThreadFactoryBuilder {
        format(nameFormat, 0)
        this.nameFormat = nameFormat
        return this
    }

    fun setDaemon(daemon: Boolean): ThreadFactoryBuilder {
        this.daemon = daemon
        return this
    }

    fun setPriority(priority: Int): ThreadFactoryBuilder {
        require(priority >= 1) { "Thread priority ($priority) must be >= 1"}
        require(priority <= 10) { "Thread priority ($priority) must be >= 10"}
        this.priority = priority
        return this
    }

    fun setUncaughtExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler): ThreadFactoryBuilder {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler
        return this
    }

    fun setThreadFactory(backingThreadFactory: ThreadFactory): ThreadFactoryBuilder {
        this.backingThreadFactory = backingThreadFactory
        return this
    }

    fun build(): ThreadFactory {
        return doBuild(this)
    }

    companion object {

        private fun doBuild(builder: ThreadFactoryBuilder): ThreadFactory {

            val nameFormat = builder.nameFormat
            val daemon = builder.daemon
            val priority = builder.priority
            val uncaughtExceptionHandler = builder.uncaughtExceptionHandler
            val backingThreadFactory: ThreadFactory =builder.backingThreadFactory ?: Executors.defaultThreadFactory()

            val count = if (nameFormat != null) {
                atomic(0L)
            } else {
                null
            }

            return ThreadFactory { runnable ->

                val thread = backingThreadFactory.newThread(runnable)

                if (nameFormat != null) {
                    thread.name = format(nameFormat,count!!.getAndIncrement())
                }

                if (daemon != null) {
                    thread.isDaemon = daemon
                }

                if (priority != null) {
                    thread.priority = priority
                }

                if (uncaughtExceptionHandler != null) {
                    thread.uncaughtExceptionHandler = uncaughtExceptionHandler
                }

                thread
            }

        }

        private fun format(format: String, vararg args: Any): String {
            return String.format(Locale.ROOT, format, *args)
        }

    }
}
