package hr.mlovrekov.gdx.console.parser

import hr.mlovrekov.gdx.console.token.CommandNameParser
import hr.mlovrekov.gdx.console.token.ParameterParser
import hr.mlovrekov.gdx.console.token.TypeParser
import com.badlogic.gdx.utils.Array as GdxArray

class TokenBasedConsoleParser(val typeParsers: Array<TypeParser<*>>) : ConsoleParser {

    private val commandNameParser = CommandNameParser()

    override fun parse(line: String): ParseResult {
        val input = Input(line)
        val parameters = Parameters()

        val parameterParser = ParameterParser(parameters)

        if (input.isEmpty()) {
            throw ParseException(input.index, "No input to parse")
        }

        if (!commandNameParser.canParse(input)) {
            throw ParseException(input.index, "Invalid symbol '${input.peek()}' in command name at column ${input.index + 1}")
        }

        val commandName = commandNameParser.parse(input, this)

        while (!input.isEol()) {
            input.skipWhitespace()
            if (!parameterParser.canParse(input)) {
                throw ParseException(input.index, "Invalid symbol '${input.peek()}' in parameter at column ${input.index + 1}")
            }

            parameterParser.parse(input, this)
        }

        return ParseResult(commandName, parameters)
    }

    fun parseToken(input: Input): Any? {
        input.begin()
        typeParsers
                .filter { it.canParse(input) }
                .forEach {
                    try {
                        return it.parse(input, this)
                    } catch (ex: ParseException) {
                        input.rollback()
                        if (typeParsers.indexOf(it) == typeParsers.lastIndex) {
                            throw ex
                        }
                    }
                }
        throw ParseException(input.index,
                             "Unknown symbol '${input.peek()}' at column ${input.index + 1}")
    }

}