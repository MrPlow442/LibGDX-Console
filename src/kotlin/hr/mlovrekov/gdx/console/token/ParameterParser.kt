package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class ParameterParser(val parameters: Parameters) : TypeParser<Parameters> {
    companion object {
        const val PARAMETER_PREFIX = '-'
        const val PARAMETER_KEY_VALUE_SEPARATOR = '='
    }

    override fun canParse(input: Input): Boolean {
        return !input.isAtChar(PARAMETER_KEY_VALUE_SEPARATOR)
    }

    override fun parse(input: Input, parser: TokenBasedConsoleParser): Parameters {
        val key = StringBuilder()
//        input.increment()
        while (!input.isEol()) {
            if (input.isAtWhitespace()) {
                break
            }
            if (!input.isAtChar(PARAMETER_KEY_VALUE_SEPARATOR)) {
                key.append(input.getAndIncrement())
                continue
            } else {
                if (input.hasNext()) {
                    input.increment()
                    parameters.put(parser.parseToken(input), key.toString())
                    return parameters
                } else {
                    throw ParseException(input.index + 1,
                                         "Unexpected EOL on column ${input.index + 2}")
                }
            }
        }
        parameters.add(key.toString())
        return parameters
    }

}