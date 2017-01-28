package hr.mlovrekov.gdx.console.buffer

import com.badlogic.gdx.utils.Array as GdxArray

interface ConsoleBuffer<out T> {
    val buffer: T
    fun write(message: String): String
    fun clear()

    fun addObserver(observer: ConsoleBufferObserver)

    fun removeObserver(observer: ConsoleBufferObserver)

    fun removeObservers()

    fun notifyObservers()
}