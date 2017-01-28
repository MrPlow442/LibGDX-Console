package hr.mlovrekov.gdx.console

import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.buffer.ArrayConsoleBuffer
import hr.mlovrekov.gdx.console.buffer.ConsoleBuffer
import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.command.HelpCommand
import hr.mlovrekov.gdx.console.command.ListCommand
import hr.mlovrekov.gdx.console.command.SayCommand
import hr.mlovrekov.gdx.console.history.ArrayConsoleHistory
import hr.mlovrekov.gdx.console.history.ConsoleHistory
import hr.mlovrekov.gdx.console.parser.ConsoleParser
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser
import hr.mlovrekov.gdx.console.token.*

class ConsoleConfiguration {
    companion object {
        private const val DEFAULT_MAX_ROWS = 25
        private const val DEFAULT_MAX_HISTORY = 25
        private const val DEFAULT_COMMAND_PREFIX = '>'

        val DEFAULT_CONSOLE_CONFIGURATION = ConsoleConfiguration()
    }

    var consoleBuffer: ConsoleBuffer<*> = ArrayConsoleBuffer(DEFAULT_MAX_ROWS)
        private set
    var commandPrefix = DEFAULT_COMMAND_PREFIX
        private set
    var consoleHistory: ConsoleHistory = ArrayConsoleHistory(DEFAULT_MAX_HISTORY)
        private set
    var consoleParser: ConsoleParser = TokenBasedConsoleParser(arrayOf(StringTypeParser(),
                                                                       BooleanTypeParser(BooleanTypeParser.BooleanKeyword.TRUE),
                                                                       BooleanTypeParser(BooleanTypeParser.BooleanKeyword.FALSE),
                                                                       ColorTypeParser(),
                                                                       NullTypeParser(),
                                                                       NumberTypeParser(),
                                                                       ArrayTypeParser(),
                                                                       MapTypeParser()))
        private set
    var commands = ObjectMap<String, ConsoleCommand>(10)
        private set

    init {
        registerCommand("list", ListCommand())
        registerCommand("help", HelpCommand())
        registerCommand("say", SayCommand())
    }

    fun setConsoleParser(consoleParser: ConsoleParser): ConsoleConfiguration {
        this.consoleParser = consoleParser
        return this
    }

    fun setConsoleBuffer(consoleBuffer: ConsoleBuffer<*>): ConsoleConfiguration {
        this.consoleBuffer = consoleBuffer
        return this
    }

    fun setConsoleHistory(consoleHistory: ConsoleHistory): ConsoleConfiguration {
        this.consoleHistory = consoleHistory
        return this
    }

    fun setCommandPrefix(commandPrefix: Char): ConsoleConfiguration {
        this.commandPrefix = commandPrefix
        return this
    }

    fun registerCommand(commandName: String, command: ConsoleCommand): ConsoleConfiguration {
        this.commands.put(commandName, command)
        return this
    }
}