package hr.mlovrekov.gdx.console.history

import com.badlogic.gdx.utils.Array as GdxArray

class ArrayConsoleHistory(private val maxHistory: Int) : ConsoleHistory {
    private val history = GdxArray<String>(maxHistory)
    private var index = -1

    override fun addEntry(entry: String) {
        if (entry in history) {
            return
        }

        if (history.size == maxHistory) {
            history.removeIndex(0)
        }
        history.add(entry)
        index = history.size - 1
    }

    override fun previous(): String {
        if (history.size == 0) {
            return ""
        }

        return if (index > 0) {
            history[index--]
        } else {
            history[index]
        }
    }

    override fun next(): String {
        if (history.size == 0) {
            return ""
        }

        return if (index < (history.size - 1)) {
            history[++index]
        } else {
            ""
        }
    }
}