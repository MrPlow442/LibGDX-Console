package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

private object NumberTypeConstants {
    const val MINUS = '-'
    const val DECIMAL_SEPARATOR = '.'
    const val DOUBLE_SUFFIX = 'd'
}

class IntegerType : Type<Int> {

    override fun canParse(input: InspectableInput): Boolean {
        if (!input.isAtDigit() && !input.isAtChar(NumberTypeConstants.MINUS)) {
            return false
        }

        var index = input.index

        if (input.isChar(index, NumberTypeConstants.MINUS)) {
            ++index
        }

        while (input.isDigit(index)) {
            if (input.isChar(index + 1, NumberTypeConstants.DECIMAL_SEPARATOR)) {
                return false
            }
            ++index
        }

        return true
    }

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): Int {
        val integerBuilder = StringBuilder()

        if (input.isAtChar(NumberTypeConstants.MINUS)) {
            integerBuilder.append(input.getAndIncrement())
        }

        while (input.isAtDigit()) {
            integerBuilder.append(input.getAndIncrement())
        }

        return integerBuilder.toString().toInt()
    }

}

class FloatType : Type<Float> {

    override fun canParse(input: InspectableInput): Boolean {
        if (!input.isAtDigit() && !input.isAtChar(NumberTypeConstants.MINUS)) {
            return false
        }

        var index = input.index
        var isFloat = false

        if (input.isChar(index, NumberTypeConstants.MINUS)) {
            ++index
        }

        while (input.isDigit(index) || input.isChar(index, NumberTypeConstants.DECIMAL_SEPARATOR)) {
            if (input.isChar(index, NumberTypeConstants.DECIMAL_SEPARATOR) &&
                input.isDigit(index - 1) &&
                input.isDigit(index + 1)) {
                isFloat = true
            }
            ++index
        }

        return isFloat && !input.isChar(index, NumberTypeConstants.DOUBLE_SUFFIX)
    }

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): Float {
        val floatBuilder = StringBuilder()
        val startIndex = input.index
        var isFloat = false

        if (input.isAtChar(NumberTypeConstants.MINUS)) {
            floatBuilder.append(input.getAndIncrement())
        }

        while (input.isAtDigit() || input.isAtChar(NumberTypeConstants.DECIMAL_SEPARATOR)) {
            if (input.isAtChar(NumberTypeConstants.DECIMAL_SEPARATOR)) {
                if (!input.previousIsDigit() || !input.nextIsDigit()) {
                    throw ParseException(input.index, "Unexpected '${NumberTypeConstants.DECIMAL_SEPARATOR}' on column ${input.index + 1}")
                }
                isFloat = true
            }
            floatBuilder.append(input.getAndIncrement())
        }

        if (!isFloat) {
            throw ParseException(startIndex, "Value '$floatBuilder' is not a floating point number")
        }

        return floatBuilder.toString().toFloat()
    }
}

class DoubleType : Type<Double> {
    override fun canParse(input: InspectableInput) : Boolean {
        if (!input.isAtDigit() && !input.isAtChar(NumberTypeConstants.MINUS)) {
            return false
        }

        var index = input.index
        var isFloat = false

        if (input.isChar(index, NumberTypeConstants.MINUS)) {
            ++index
        }

        while (input.isDigit(index) || input.isChar(index, NumberTypeConstants.DECIMAL_SEPARATOR)) {
            if (input.isChar(index, NumberTypeConstants.DECIMAL_SEPARATOR) &&
                input.isDigit(index - 1) &&
                input.isDigit(index + 1)) {
                isFloat = true
            }
            ++index
        }

        return isFloat && input.isChar(index, NumberTypeConstants.DOUBLE_SUFFIX)
    }

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): Double {
        val doubleBuilder = StringBuilder()
        val startIndex = input.index
        var isFloat = false

        if (input.isAtChar(NumberTypeConstants.MINUS)) {
            doubleBuilder.append(input.getAndIncrement())
        }

        while (input.isAtDigit() || input.isAtChar(NumberTypeConstants.DECIMAL_SEPARATOR)) {
            if (input.isAtChar(NumberTypeConstants.DECIMAL_SEPARATOR)) {
                if (!input.previousIsDigit() || !input.nextIsDigit()) {
                    throw ParseException(input.index, "Unexpected '${NumberTypeConstants.DECIMAL_SEPARATOR}' on column ${input.index + 1}")
                }
                isFloat = true
            }
            doubleBuilder.append(input.getAndIncrement())
        }

        if (!isFloat) {
            throw ParseException(startIndex, "Value '$doubleBuilder' is not a floating point number")
        }

        if (!input.isAtChar(NumberTypeConstants.DOUBLE_SUFFIX)) {
            throw ParseException(input.index, "Expected double type suffix '${NumberTypeConstants.DOUBLE_SUFFIX}' missing")
        }

        input.increment()

        return doubleBuilder.toString().toDouble()
    }

}