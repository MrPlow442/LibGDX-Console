package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

class MapType : Type<ObjectMap<Any, Any?>> {

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
        const val KEY_VALUE_SEPARATOR_SYMBOL = ':'
        const val LIST_SEPARATOR_SYMBOL = ','
    }

    override fun canParse(input: InspectableInput) = input.peek() == OPEN_MAP_SYMBOL

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): ObjectMap<Any, Any?> {
        val output = ObjectMap<Any, Any?>()

        if(input.nextIsChar(CLOSE_MAP_SYMBOL, true)) {
            input.increment(input.indexOf(CLOSE_MAP_SYMBOL) + 1 - input.index)
            return output
        }
        
        var mapState = MapState.EXPECTING_KEY
        val mapOpenIndex = input.index
        input.increment()
        var key: Any? = null
        while (!input.isEol()) {
            input.skipWhitespace()
            if (input.isAtChar(CLOSE_MAP_SYMBOL)) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.FINISHED
                    break
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$CLOSE_MAP_SYMBOL' on column ${input.index + 1}")
                }
            } else if (input.isAtChar(LIST_SEPARATOR_SYMBOL)) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_KEY
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$LIST_SEPARATOR_SYMBOL' on column ${input.index + 1}")
                }
            } else if (input.isAtChar(KEY_VALUE_SEPARATOR_SYMBOL)) {
                if (mapState == MapState.EXPECTING_KEY_VALUE_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_VALUE
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$KEY_VALUE_SEPARATOR_SYMBOL' on column ${input.index + 1}")
                }
            } else if (mapState == MapState.EXPECTING_KEY) {
                val keyIndex = input.index
                key = parser.parseToken(input)
                if (key != null) {
                    mapState = MapState.EXPECTING_KEY_VALUE_SEPARATOR
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Invalid value on column ${keyIndex + 1}. Map keys can't be null")
                }
            } else if (mapState == MapState.EXPECTING_VALUE) {
                if (key != null) {
                    val value = parser.parseToken(input)
                    output.put(key, value)
                    mapState = MapState.EXPECTING_LIST_SEPARATOR
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Key for this associated value is somehow null.")
                }
            } else {
                throw ParseException(input.index, "Unexpected symbol '${input.peek()}' at column ${input.index + 1}")
            }
        }

        if (mapState != MapState.FINISHED) {
            throw ParseException(mapOpenIndex,
                                 "Missing '$CLOSE_MAP_SYMBOL' for map opened on column ${mapOpenIndex + 1}")
        }

        return output
    }

}