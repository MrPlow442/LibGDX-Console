package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArrayTypeTest {

    private val arrayType = ArrayType()
    private val parser = TokenConsoleParser(commands = arrayListOf(),
                                    types = arrayOf(StringType(),
                                                    LiteralType("true", true),
                                                    LiteralType("false", false),
                                                    LiteralType("null", null),
                                                    ColorType(),
                                                    DoubleType(),
                                                    FloatType(),
                                                    IntegerType(),
                                                    arrayType,
                                                    MapType()))

    @Test
    fun parse() {
        val input = Input("[1, 2, 3] [1, 1.0, 1.0d] [1, hello, \"Hello world!\"]")

        assertTrue(arrayType.canParse(input))

        val firstArray = arrayType.parse(input, parser)

        assertEquals(1, firstArray[0])
        assertEquals(2, firstArray[1])
        assertEquals(3, firstArray[2])

        input.increment()

        val secondArray = arrayType.parse(input, parser)

        assertEquals(1, secondArray[0])
        assertEquals(1.0f, secondArray[1] as Float, 0.05f)
        assertEquals(1.0, secondArray[2] as Double, 0.05)

        input.increment()

        val thirdArray = arrayType.parse(input, parser)

        assertEquals(1, thirdArray[0])
        assertEquals("hello", thirdArray[1] as String)
        assertEquals("Hello world!", thirdArray[2] as String)
    }
}