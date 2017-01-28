package hr.mlovrekov.gdx.console.token

import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class MapTypeParser : TypeParser<ObjectMap<Any, Any?>> {

    private enum class MapState {
        EXPECTING_KEY,
        EXPECTING_KEY_VALUE_SEPARATOR,
        EXPECTING_VALUE,
        EXPECTING_LIST_SEPARATOR,
        FINISHED
    }

    companion object {
        const val OPEN_MAP_SYMBOL = '{'
        const val CLOSE_MAP_SYMBOL = '}'
        const val KEY_VALUE_SEPARATOR = ':'
        const val LIST_SEPARATOR = ','
    }

    override fun canParse(input: Input) = input.peek() == OPEN_MAP_SYMBOL

    override fun parse(input: Input, parser: TokenBasedConsoleParser): ObjectMap<Any, Any?> {
        val output = ObjectMap<Any, Any?>()
        var mapState = MapState.EXPECTING_KEY
        val mapOpenIndex = input.index
        input.increment()
        var key: Any? = null
        while (!input.isEol()) {
            input.skipWhitespace()
            if (input.peek() == CLOSE_MAP_SYMBOL) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.FINISHED
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$CLOSE_MAP_SYMBOL' on column ${input.index + 1}")
                }
            }
            if (input.peek() == LIST_SEPARATOR) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_KEY
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$LIST_SEPARATOR' on column ${input.index + 1}")
                }
            }
            if (input.peek() == KEY_VALUE_SEPARATOR) {
                if (mapState == MapState.EXPECTING_KEY_VALUE_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_VALUE
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$KEY_VALUE_SEPARATOR' on column ${input.index + 1}")
                }
            }
            if (mapState == MapState.EXPECTING_KEY) {
                val keyIndex = input.index
                key = parser.parseToken(input)
                if (key != null) {
                    mapState = MapState.EXPECTING_KEY_VALUE_SEPARATOR
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Invalid value on column ${keyIndex + 1}. Map keys can't be null")
                }
            }
            if (mapState == MapState.EXPECTING_VALUE) {
                if (key != null) {
                    val value = parser.parseToken(input)
                    output.put(key, value)
                    mapState = MapState.EXPECTING_LIST_SEPARATOR
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Key for this associated value is somehow null.")
                }
            }
        }

        if (mapState != MapState.FINISHED) {
            throw ParseException(mapOpenIndex,
                                 "Missing '$CLOSE_MAP_SYMBOL' for map opened on column ${mapOpenIndex + 1}")
        }

        return output
    }

}