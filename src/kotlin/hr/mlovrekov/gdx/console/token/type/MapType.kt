package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

class MapType(private val openMapSymbol: Char = DEFAULT_OPEN_MAP_SYMBOL,
              private val closeMapSymbol: Char = DEFAULT_CLOSE_MAP_SYMBOL,
              private val keyValueSeparatorSymbol: Char = DEFAULT_KEY_VALUE_SEPARATOR_SYMBOL,
              private val listSeparatorSymbol: Char = DEFAULT_LIST_SEPARATOR_SYMBOL) : Type<ObjectMap<Any, Any?>> {

    private enum class MapState {
        EXPECTING_KEY,
        EXPECTING_KEY_VALUE_SEPARATOR,
        EXPECTING_VALUE,
        EXPECTING_LIST_SEPARATOR,
        FINISHED
    }

    companion object {
        const val DEFAULT_OPEN_MAP_SYMBOL = '{'
        const val DEFAULT_CLOSE_MAP_SYMBOL = '}'
        const val DEFAULT_KEY_VALUE_SEPARATOR_SYMBOL = ':'
        const val DEFAULT_LIST_SEPARATOR_SYMBOL = ','
    }

    override fun canParse(input: InspectableInput) = input.peek() == openMapSymbol

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): ObjectMap<Any, Any?> {
        val output = ObjectMap<Any, Any?>()
        var mapState = MapState.EXPECTING_KEY
        val mapOpenIndex = input.index
        input.increment()
        var key: Any? = null
        while (!input.isEol()) {
            input.skipWhitespace()
            if (input.isAtChar(closeMapSymbol)) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.FINISHED
                    break
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$closeMapSymbol' on column ${input.index + 1}")
                }
            } else if (input.isAtChar(listSeparatorSymbol)) {
                if (mapState == MapState.EXPECTING_LIST_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_KEY
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$listSeparatorSymbol' on column ${input.index + 1}")
                }
            } else if (input.isAtChar(keyValueSeparatorSymbol)) {
                if (mapState == MapState.EXPECTING_KEY_VALUE_SEPARATOR) {
                    input.increment()
                    mapState = MapState.EXPECTING_VALUE
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '$keyValueSeparatorSymbol' on column ${input.index + 1}")
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
                                 "Missing '$closeMapSymbol' for map opened on column ${mapOpenIndex + 1}")
        }

        return output
    }

}