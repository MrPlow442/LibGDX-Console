package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class StringTypeTest {

    private val type = StringType()
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test
    fun parseWrapped() {
        val input = Input("\"Hello world!\"")

        assertTrue(type.canParse(input))
        assertEquals("Hello world!", type.parse(input, parser))
    }

    @Test
    fun parsePlain() {
        val input = Input("Hello_world! Goodbye")

        assertTrue(type.canParse(input))
        assertEquals("Hello_world", type.parse(input, parser))
    }

    @Test
    fun parseEscape() {
        val input = Input("\"Hello \\\"World\\\"!\"")

        assertTrue(type.canParse(input))
        assertEquals("Hello \"World\"!", type.parse(input, parser))
    }

    @Test(expected = ParseException::class)
    fun parseMissingClosingQuotes() {
        val input = Input("\"Hello world!")

        assertTrue(type.canParse(input))
        type.parse(input, parser)
    }

}