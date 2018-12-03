package catt.mvp.sample.base.adm

import android.app.Activity
import java.util.*

internal class BaseActivityStack : IStack<Activity> {
    private val stack: Stack<Activity> by lazy { Stack<Activity>() }
    override fun push(target: Activity) {
        synchronized(target) {
            stack.remove(target)
            stack.push(target)
            return@synchronized
        }
    }

    override fun pop() {
        peek()?.apply {
            finish()
            stack.pop()
        }
    }

    override fun remove(target: Activity) {
        stack.remove(target)
    }

    override fun peek(): Activity? {
        if (empty()) return null
        return stack.peek()
    }

    override fun empty(): Boolean = stack.empty()

    override fun search(target: Activity): Activity? {
        val absoluteIndex = stack.search(target)
        return when (!stack.empty() && absoluteIndex != -1) {
            true -> stack[absoluteIndex - 1]
            false -> null
        }
    }

    fun <T> search(clazz:Class<T>): T? {
        for (index in stack.indices.reversed()) {
            if(stack[index]::class.java.name == clazz.name){
                return stack[index] as T
            }
        }
        return null
    }


    internal fun killMyPid() {
        for (index in stack.indices.reversed()) {
            val aty = stack[index]
            aty.finish()
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    companion object {
        private object Single {
            internal val INSTANCE: BaseActivityStack by lazy { BaseActivityStack() }
        }

        @JvmStatic
        fun get(): BaseActivityStack = Single.INSTANCE
    }
}