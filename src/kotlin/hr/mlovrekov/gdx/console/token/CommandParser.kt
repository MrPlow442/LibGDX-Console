package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException

class CommandParser {
    fun canParse(input: Input) = input.isAtLetter()
    fun parse(input: Input, commands: List<ConsoleCommand>): ConsoleCommand {
        val startIndex = input.index
        val commandName = input.grabNextUntilWhitespace()
        return commands.find { it.name == commandName } ?: throw ParseException(startIndex, "Unknown command '$commandName'")
    }
}