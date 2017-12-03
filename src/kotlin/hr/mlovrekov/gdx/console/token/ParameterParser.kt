package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.parser.*

class ParameterParser {
    companion object {
        const val PARAMETER_KEY_VALUE_SEPARATOR = '='
    }

    fun canParse(input: Input) = !input.isEol()

    fun parse(input: TraversableInput, command: ConsoleCommand, parser: TokenConsoleParser): Parameters {
        val parameters = Parameters()

        while (!input.isEol()) {
            input.skipWhitespace()
            val lastIndex = command.parameterDefinitions.size - 1
            for (i in 0..lastIndex) {
                val it = command.parameterDefinitions[i]
                val key = it.key
                val endIndex = input.index + key.length
                if (input.matches(key) &&
                    (input.isEol(index = endIndex) || input.isWhitespace(index = endIndex)) &&
                    !input.isChar(index = endIndex,
                                  char = PARAMETER_KEY_VALUE_SEPARATOR,
                                  ignoreWhitespace = true)) {
                    // plain non-value parameter
                    parameters.add(input.grabNextUntilWhitespace())
                    break
                } else if (it is ValueParameterDefinition<*, *> &&
                           input.matches(key) &&
                           input.isChar(index = endIndex,
                                        char = PARAMETER_KEY_VALUE_SEPARATOR,
                                        ignoreWhitespace = true)) {
                    // value parameter
                    input.increment(key.length + 1)
                    input.skipWhitespace()

                    val type = parser.getType(it.type)
                               ?: throw ParseException(input.index, "Unknown value type '${it.type.simpleName}' for parameter '$key'")

                    if (!type.canParse(input)) {
                        throw ParseException(input.index, "Invalid value for parameter '$key'")
                    }

                    parameters.put(key, type.parse(input, parser))
                    break
                } else if (it is ValueParameterDefinition<*, *> &&
                           key == ValueParameterDefinition.PRIMARY_KEY &&
                           parser.getType(it.type)?.canParse(input) == true) {
                    // implicit value parameter
                    val type = parser.getType(it.type)
                               ?: throw ParseException(input.index, "Unknown value type '${it.type.simpleName}' for implicit parameter")

                    if (!type.canParse(input)) {
                        throw ParseException(input.index, "Invalid value for implicit parameter")
                    }

                    parameters.put(ValueParameterDefinition.PRIMARY_KEY, type.parse(input, parser))
                    break
                } else if(i == lastIndex) {
                    throw ParseException(input.index, "Invalid symbol '${input.peek()}' at column ${input.index + 1}")
                }

            }
        }

        return parameters
    }

}