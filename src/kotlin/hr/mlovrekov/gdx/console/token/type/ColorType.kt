package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser

class ColorType : Type<Color> {

    companion object {
        const val HEX_COLOR_PREFIX = '#'
        const val RGB_HEX_COLOR_SIZE = 6
        const val RGBA_HEX_COLOR_SIZE = 8
    }

    override fun canParse(input: Input): Boolean {
        fun isHex(input: Input): Boolean {
            return input.peek() == HEX_COLOR_PREFIX && (input.hasNext(RGB_HEX_COLOR_SIZE) || input.hasNext(RGBA_HEX_COLOR_SIZE))
        }

        fun isLiteral(input: Input): Boolean {
            return Colors.getColors().keys().any { input.matches(it) }
        }

        return isHex(input) || isLiteral(input)
    }

    override fun parse(input: Input, parser: TokenConsoleParser): Color {
        return if (input.isAtChar(HEX_COLOR_PREFIX)) {
            input.increment()
            when {
                input.hasNext(RGBA_HEX_COLOR_SIZE - 1) -> parseHexRgba(input)
                input.hasNext(RGB_HEX_COLOR_SIZE - 1)  -> parseHexRgb(input)
                else                                                                                           -> throw ParseException(input.index, "Invalid color code")
            }
        } else {
            parseLiteral(input)
        }
    }

    private fun parseHexRgb(input: Input): Color {
        val index = input.index
        val hex = input.grabNext(RGB_HEX_COLOR_SIZE)
        try {
            val r = hexToFloat(hex.substring(0, 2))
            val g = hexToFloat(hex.substring(2, 4))
            val b = hexToFloat(hex.substring(4, 6))
            return Color(r, g, b, 1f)
        } catch (ex: NumberFormatException) {
            throw ParseException(index, "Invalid hexadecimal color code")
        }
    }

    private fun parseHexRgba(input: Input): Color {
        val index = input.index
        val hex = input.grabNext(RGBA_HEX_COLOR_SIZE)
        try {
            val r = hexToFloat(hex.substring(0, 2))
            val g = hexToFloat(hex.substring(2, 4))
            val b = hexToFloat(hex.substring(4, 6))
            val a = hexToFloat(hex.substring(6, 8))
            return Color(r, g, b, a)
        } catch (ex: NumberFormatException) {
            throw ParseException(index, "Invalid hexadecimal color code")
        }
    }

    private fun hexToFloat(hexColorCodePiece: String): Float {
        return Integer.parseInt(hexColorCodePiece, 16).toFloat() / 255.0f
    }

    private fun parseLiteral(input: Input): Color {
        val literal = input.grabAllNext()
        if (!Colors.getColors().containsKey(literal)) {
            throw ParseException(input.index, "Invalid color literal")
        }

        return Colors.get(literal)
    }

}