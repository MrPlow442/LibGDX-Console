package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors
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

        fun isLiteral(input: InspectableInput) = Colors.getColors().keys().any { input.matches(it) }

        return isHex(input) || isLiteral(input)
    }

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): Color {
        return if (input.isAtChar(HEX_COLOR_PREFIX)) {
            val startIndex = input.index
            try {
                Color.valueOf(input.grabNextUntilWhitespace())
            } catch (e: Exception) {
                throw ParseException(startIndex, "Invalid hex code at column ${startIndex + 1}")
            }
        } else {
            val literal = input.grabNextUntilWhitespace()
            if (!Colors.getColors().containsKey(literal)) {
                throw ParseException(input.index, "Invalid color literal")
            }
            Colors.get(literal)
        }
    }

}