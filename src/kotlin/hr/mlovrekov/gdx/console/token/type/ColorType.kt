package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

class ColorType : Type<Color> {

    companion object {
        const val HEX_COLOR_PREFIX = '#'
        const val RGB_HEX_COLOR_SIZE = 6
        const val RGBA_HEX_COLOR_SIZE = 8
    }

    override fun canParse(input: InspectableInput): Boolean {
        fun isHex(input: InspectableInput) =
                input.peek() == HEX_COLOR_PREFIX && (input.hasNext(RGB_HEX_COLOR_SIZE) || input.hasNext(RGBA_HEX_COLOR_SIZE))

        return isHex(input)
    }

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): Color {
        if (input.isAtChar(HEX_COLOR_PREFIX)) {
            val startIndex = input.index
            val hexBuilder = StringBuilder()
            hexBuilder.append(input.getAndIncrement())
            try {
                while (input.isAtDigit() || input.isAtOneOf('A', 'B', 'C', 'D', 'E', 'F')) {
                    hexBuilder.append(input.getAndIncrement())
                }
                return Color.valueOf(hexBuilder.toString())
            } catch (e: Exception) {
                throw ParseException(startIndex, "Invalid hex code at column ${startIndex + 1}")
            }
        } else {
            throw ParseException(input.index, "Invalid hex code at column ${input.index + 1}")
        }
    }

}