package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class LiteralTypeTest {
    private val trueType = LiteralType("true", true)
    private val falseType = LiteralType("false", false)
    private val nullType = LiteralType("null", null)
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test
    fun parseTrue() {
        val input = Input("true asdf")

        assertTrue(trueType.canParse(input))
        assertEquals(true, trueType.parse(input, parser))

        input.increment()

        assertFalse(trueType.canParse(input))
    }

    @Test
    fun parseFalse() {
        val input = Input("false asdf")

        assertTrue(falseType.canParse(input))
        assertEquals(false, falseType.parse(input, parser))

        input.increment()

        assertFalse(falseType.canParse(input))
    }

    @Test
    fun parseNull() {
        val input = Input("null asdf")

        assertTrue(nullType.canParse(input))
        assertNull(nullType.parse(input, parser))

        input.increment()

        assertFalse(nullType.canParse(input))
    }
}