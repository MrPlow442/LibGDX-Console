package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.utils.StringBuilder
import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

class StringType : Type<String> {
    companion object {
        const val STRING_WRAP_SYMBOL = '"'
        const val ESCAPE_SYMBOL = '\\'
    }

    override fun canParse(input: InspectableInput) = input.isAtChar(STRING_WRAP_SYMBOL) || input.isAtLetter()

    override fun parse(input: TraversableInput, parser: TokenConsoleParser) = when {
        input.isAtChar(STRING_WRAP_SYMBOL) -> parseWrapped(input)
        input.isAtLetter()                 -> parseSimple(input)
        else                               -> throw ParseException(input.index, "Character '${input.peek()}' not expected here")
    }

    private fun parseWrapped(input: TraversableInput): String {
        val stringStartIndex = input.index
        input.increment()
        val output = StringBuilder()
        var finished = false
        while (!input.isEol()) {
            if (input.isAtChar(ESCAPE_SYMBOL)) {
                if (input.hasNext()) {
                    when (input.peekNext()) {
                        'n'  -> output.append('\n')
                        'r'  -> output.append('\r')
                        '\\' -> output.append('\\')
                        '"'  -> output.append('\"')
                    }
                    input.increment(2)
                }
            }
            if (input.isAtChar(STRING_WRAP_SYMBOL)) {
                finished = true
                input.increment()
                break
            }
            output.append(input.getAndIncrement())
        }

        if (!finished) {
            throw ParseException(stringStartIndex,
                                 "Missing closing '$STRING_WRAP_SYMBOL' for string on column ${stringStartIndex + 1}")
        }

        return output.toString()
    }

    private fun parseSimple(input: TraversableInput): String {
        val output = StringBuilder()
        while (input.isAtLetterOrDigit() || input.isAtChar('_')) {
            output.append(input.getAndIncrement())
        }

        return output.toString()
    }

}