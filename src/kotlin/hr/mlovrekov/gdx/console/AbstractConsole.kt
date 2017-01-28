package hr.mlovrekov.gdx.console

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.buffer.ConsoleBuffer
import hr.mlovrekov.gdx.console.buffer.ConsoleBufferObserver
import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.history.ConsoleHistory
import hr.mlovrekov.gdx.console.parser.ConsoleParser
import hr.mlovrekov.gdx.console.parser.ParseException
import com.badlogic.gdx.utils.Array as GdxArray

abstract class AbstractConsole(consoleConfiguration: ConsoleConfiguration) : ApplicationLogger {

    protected val buffer: ConsoleBuffer<*> = consoleConfiguration.consoleBuffer
    protected val history: ConsoleHistory = consoleConfiguration.consoleHistory
    protected val commandPrefix: Char = consoleConfiguration.commandPrefix
    protected val parser: ConsoleParser = consoleConfiguration.consoleParser

    val commands = ObjectMap<String, ConsoleCommand>(10)

    private var previousApplicationLogger: ApplicationLogger? = null

    init {
        consoleConfiguration.commands.forEach { registerCommand(it.key, it.value) }
    }

    fun printLine(message: String) = buffer.write(message)

    fun execute(line: String) {
        printLine("$commandPrefix$line")
        history.addEntry(line)
        try {
            val parseResult = parser.parse(line)

            if (!commands.containsKey(parseResult.commandName)) {
                printLine("Unknown command '${parseResult.commandName}'")
                return
            }

            commands[parseResult.commandName].execute(this, parseResult.parameters)
        } catch (ex: ParseException) {
            printLine(ex.message)
        }
    }

    fun registerCommand(commandName: String, command: ConsoleCommand) {
        commands.put(commandName, command)
    }

    fun deregisterCommand(commandName: String) {
        commands.remove(commandName)
    }

    fun previousHistory(): String = history.previous()

    fun nextHistory(): String = history.next()

    fun addBufferObserver(observer: ConsoleBufferObserver) = buffer.addObserver(observer)

    fun removeBufferObserver(observer: ConsoleBufferObserver) = buffer.removeObserver(observer)

    fun removeBufferObservers() = buffer.removeObservers()

    override fun log(tag: String, message: String) {
        printLine("$tag:$message")
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        printLine("$tag:$message ${exception.message}")
    }

    override fun error(tag: String, message: String) {
        printLine("$tag:$message")
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        printLine("$tag:$message ${exception.message}")
    }

    override fun debug(tag: String, message: String) {
        printLine("$tag:$message")
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        printLine("$tag:$message ${exception.message}")
    }

    fun registerAsApplicationLogger() {
        previousApplicationLogger = Gdx.app.applicationLogger
        Gdx.app.applicationLogger = this
    }

    fun deregeisterAsApplicationLogger() {
        Gdx.app.applicationLogger = previousApplicationLogger
    }

    override fun toString(): String {
        return buffer.toString()
    }
}


