package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.utils.StringBuilder
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser

class NumberType : Type<Number> {
    companion object {
        const val MINUS = '-'
        const val DECIMAL_SEPARATOR = '.'
    }

    override fun canParse(input: Input) = input.isAtDigit() || (input.isAtChar(MINUS) && input.nextIsAtDigit())

    override fun parse(input: Input, parser: TokenConsoleParser): Number {
        val numberStringBuilder = StringBuilder()
        var isDecimal = false

        if (input.isAtChar(MINUS)) {
            numberStringBuilder.append(input.getAndIncrement())
        }

        while (!input.isEol()) {
            if (input.isAtDigit()) {
                numberStringBuilder.append(input.getAndIncrement())
                continue
            } else if (input.isAtChar(DECIMAL_SEPARATOR)) {
                if (!isDecimal && input.previousIsAtDigit() && input.nextIsAtDigit()) {
                    isDecimal = true
                    numberStringBuilder.append(input.getAndIncrement())
                    continue
                } else {
                    throw ParseException(input.index,
                                         "Unexpected '${DECIMAL_SEPARATOR}' on column ${input.index + 1}")
                }
            } else {
                break
            }
        }

        return if (isDecimal) {
            numberStringBuilder.toString().toFloat()
        } else {
            numberStringBuilder.toString().toInt()
        }
    }
}