package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class ColorTypeTest {

    private val trueType = LiteralType("true", true)
    private val falseType = LiteralType("false", false)
    private val nullType = LiteralType("null", null)
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test
    fun parse() {
        val input = Input("true false null")

        assertTrue(trueType.canParse(input))
        assertEquals(true, trueType.parse(input, parser))

        input.increment()

        assertTrue(falseType.canParse(input))
        assertEquals(false, falseType.parse(input, parser))

        input.increment()

        assertTrue(nullType.canParse(input))
        assertNull(nullType.parse(input, parser))
    }
}