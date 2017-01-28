package hr.mlovrekov.gdx.console.token

import com.badlogic.gdx.utils.StringBuilder
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class StringTypeParser : TypeParser<String> {
    companion object {
        const val STRING_WRAP_SYMBOL = '"'
        const val ESCAPE_SYMBOL = '\\'
    }

    override fun canParse(input: Input) = input.peek() == STRING_WRAP_SYMBOL

    override fun parse(input: Input, parser: TokenBasedConsoleParser): String {
        val stringStartIndex = input.index
        input.increment()
        val output = StringBuilder(10)
        var finished = false
        while (!input.isEol()) {
            if (input.peek() == ESCAPE_SYMBOL) {
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
            if (input.peek() == STRING_WRAP_SYMBOL) {
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

}