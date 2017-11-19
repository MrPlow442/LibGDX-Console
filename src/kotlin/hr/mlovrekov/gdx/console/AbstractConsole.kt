package hr.mlovrekov.gdx.console

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.Gdx
import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.history.ConsoleHistory
import hr.mlovrekov.gdx.console.parser.ConsoleParser
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.util.ConstrainedQueue
import com.badlogic.gdx.utils.Array as GdxArray

abstract class AbstractConsole<T>(val bufferSize: Int = 50,
                                  private val commandPrefix: Char = '>') : Console, ApplicationLogger {

    abstract override val history: ConsoleHistory
    protected abstract val parser: ConsoleParser
    protected val commands = ArrayList<ConsoleCommand>(10)
    protected val lines: ConstrainedQueue<T> = ConstrainedQueue(bufferSize)

    override fun print(message: String, type: Console.LogType) {
        val evictedLine = lines.addLastReturning(buildLine(message, type))
        onPrint(lines, evictedLine)
    }

    abstract protected fun buildLine(message: String, type: Console.LogType): T

    abstract protected fun onPrint(lines: ConstrainedQueue<T>, evictedLine: T?)

    override fun execute(line: String) {
        print("$commandPrefix $line")
        try {
            val parseResult = parser.parse(line)
            parseResult.command.execute(this, commands, parseResult.parameters)
        } catch (ex: ParseException) {
            error("console", ex.message)
        } catch (ex: Exception) {
            error("console", "exception occurred command execution", ex)
        } finally {
            history.addEntry(line)
        }
    }

    @Suppress("LoopToCallChain")
    override fun getAvailableCompletions(line: String): GdxArray<String> {
        val completions = GdxArray<String>()
        if (line.isBlank()) {
            return completions
        }

        for (command in commands) {
            if (command.name.startsWith(line)) {
                completions.add(command.name)
            }
        }

        return completions
    }

    override fun registerCommand(command: ConsoleCommand) {
        commands.add(command)
    }

    override fun deregisterCommand(commandName: String) {
        commands.removeAll { it.name == commandName }
    }

    override fun log(tag: String, message: String) {
        print("[$tag]$message", Console.LogType.INFO)
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        print("[$tag]$message EXCEPTION: ${exception.message}", Console.LogType.INFO)
    }

    override fun error(tag: String, message: String) {
        print("[$tag]$message", Console.LogType.ERROR)
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        print("[$tag]$message EXCEPTION: ${exception.message}", Console.LogType.ERROR)
    }

    override fun debug(tag: String, message: String) {
        print("[$tag]$message", Console.LogType.DEBUG)
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        print("[$tag]$message EXCEPTION: ${exception.message}", Console.LogType.DEBUG)
    }
}


