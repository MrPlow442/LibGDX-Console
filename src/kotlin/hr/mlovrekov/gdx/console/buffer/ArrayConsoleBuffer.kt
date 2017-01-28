package hr.mlovrekov.gdx.console.buffer

import com.badlogic.gdx.utils.Array as GdxArray

class ArrayConsoleBuffer(val maxRows: Int) : ConsoleBuffer<GdxArray<String>> {

    private val observers = GdxArray<ConsoleBufferObserver>()
    override val buffer = GdxArray<String>(maxRows)

    override fun write(message: String): String {
        if ((buffer.size + 1) > maxRows) {
            buffer.removeIndex(0)
        }

        buffer.add(message)
        notifyObservers()
        return message
    }

    override fun clear() {
        buffer.clear()
        notifyObservers()
    }

    override fun toString() = buffer.joinToString(System.lineSeparator())

    override fun addObserver(observer: ConsoleBufferObserver) {
        if (observer !in observers) {
            observers.add(observer)
        }
    }

    override fun removeObserver(observer: ConsoleBufferObserver) {
        observers.removeValue(observer, true)
    }

    override fun removeObservers() {
        observers.clear()
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.onNotify(this)
        }
    }

}