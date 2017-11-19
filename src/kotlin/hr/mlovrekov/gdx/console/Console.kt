package hr.mlovrekov.gdx.console

import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.history.ConsoleHistory
import com.badlogic.gdx.utils.Array as GdxArray

interface Console {
    val history: ConsoleHistory
    fun execute(line: String)
    fun print(message: String, type: Console.LogType = Console.LogType.INFO)
    fun getAvailableCompletions(line: String): GdxArray<String>
    fun registerCommand(command: ConsoleCommand)
    fun deregisterCommand(commandName: String)

    enum class LogType {
        INFO,
        ERROR,
        DEBUG
    }
}