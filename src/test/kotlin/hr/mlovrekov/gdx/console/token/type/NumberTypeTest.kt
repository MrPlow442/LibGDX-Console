package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class NumberTypeTest {
    private val integerType = IntegerType()
    private val floatType = FloatType()
    private val doubleType = DoubleType()
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test
    fun parseInteger() {
        val input = Input("123 -123 12.3")

        assertTrue(integerType.canParse(input))
        assertEquals(123, integerType.parse(input, parser))

        input.increment()

        assertTrue(integerType.canParse(input))
        assertEquals(-123, integerType.parse(input, parser))

        input.increment()

        assertFalse(integerType.canParse(input))
    }

    @Test
    fun parseFloat(){
        val input = Input("12.3 -12.3 12..3")

        assertTrue(floatType.canParse(input))
        assertEquals(12.3f, floatType.parse(input, parser), 0.05f)

        input.increment()

        assertTrue(floatType.canParse(input))
        assertEquals(-12.3f, floatType.parse(input, parser), 0.05f)

        input.increment()

        assertFalse(floatType.canParse(input))
    }

    @Test
    fun parseDouble(){
        val input = Input("12.3d -12.3d 12.3")

        assertTrue(doubleType.canParse(input))
        assertEquals(12.3, doubleType.parse(input, parser), 0.05)

        input.increment()

        assertTrue(doubleType.canParse(input))
        assertEquals(-12.3, doubleType.parse(input, parser), 0.05)

        input.increment()

        assertFalse(doubleType.canParse(input))
    }

}