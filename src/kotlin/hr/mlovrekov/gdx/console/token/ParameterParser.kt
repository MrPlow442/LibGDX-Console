package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.parser.*

class ParameterParser {
    companion object {
        const val PARAMETER_KEY_VALUE_SEPARATOR = '='
    }

    fun canParse(input: Input) = input.isAtLetter()

    @Suppress("FoldInitializerAndIfToElvis")
    fun parse(input: Input, command: ConsoleCommand, parser: TokenConsoleParser, parameters: Parameters) {
        val hasValue = input.indexOf(PARAMETER_KEY_VALUE_SEPARATOR) != -1

        val key = if(hasValue) input.grabNextUntil(PARAMETER_KEY_VALUE_SEPARATOR) else input.grabNextUntilWhitespace()

        val parameterDefinition = command.parameters.find { it.key == key }

        if (parameterDefinition == null) {
            throw ParseException(input.index, "Unknown parameter '$key' on column ${input.index + 1}")
        }

        if (hasValue && parameterDefinition !is ValueParameterDefinition<*, *>) {
            throw ParseException(input.index, "Parameter '$key' missing value on column ${input.index + key.length}")
        }

        if (!input.isEol() && input.isAtChar(PARAMETER_KEY_VALUE_SEPARATOR)) {
            input.increment()
        }

        if(hasValue) {
            input.skipWhitespace()
            parameters.put(key, parser.parseToken(input))
        } else {
            parameters.add(key)
        }
    }
}