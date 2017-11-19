package hr.mlovrekov.gdx.console.history

class NullConsoleHistory : ConsoleHistory {
    override fun addEntry(entry: String) {
     //do nothing
    }

    override fun previous() = ""

    override fun next() = ""
}