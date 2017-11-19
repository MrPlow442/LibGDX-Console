package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser

class ArrayType : Type<Array<Any?>> {
    companion object {
        const val OPEN_ARRAY_SYMBOL = '['
        const val CLOSE_ARRAY_SYMBOL = ']'
        const val LIST_SEPARATOR = ','
    }

    private enum class ArrayState {
        EXPECTING_VALUE,
        EXPECTING_LIST_SEPARATOR,
        FINISHED
    }

    override fun canParse(input: Input) = input.peek() == OPEN_ARRAY_SYMBOL

    override fun parse(input: Input, parser: TokenConsoleParser): Array<Any?> {
        val output = Array<Any?>()
        var arrayState = ArrayState.EXPECTING_VALUE
        val arrayOpenIndex = input.index
        input.increment()
        while (!input.isEol()) {
            input.skipWhitespace()
            if (input.peek() == CLOSE_ARRAY_SYMBOL) {
                if (arrayState == ArrayState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    arrayState = ArrayState.FINISHED
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '${CLOSE_ARRAY_SYMBOL}' on column ${input.index + 1}")
                }
            }
            if (input.peek() == LIST_SEPARATOR) {
                if (arrayState == ArrayState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    arrayState = ArrayState.EXPECTING_VALUE
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '${LIST_SEPARATOR}' on column ${input.index + 1}")
                }
            }
            output.add(parser.parseToken(input))
            arrayState = ArrayState.EXPECTING_LIST_SEPARATOR
        }

        if (arrayState != ArrayState.FINISHED) {
            throw ParseException(arrayOpenIndex,
                                 "Missing '${CLOSE_ARRAY_SYMBOL}' for array opened on column ${arrayOpenIndex + 1}")
        }

        return output
    }
}