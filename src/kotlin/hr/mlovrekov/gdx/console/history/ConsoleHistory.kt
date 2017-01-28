package hr.mlovrekov.gdx.console.history

import com.badlogic.gdx.utils.Array as GdxArray

interface ConsoleHistory {
    fun addEntry(entry: String)
    fun previous(): String
    fun next(): String
}