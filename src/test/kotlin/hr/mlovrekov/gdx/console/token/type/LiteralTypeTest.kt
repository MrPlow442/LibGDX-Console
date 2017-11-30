package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class LiteralTypeTest {
    private val trueType = LiteralType("true", true)
    private val falseType = LiteralType("false", false)
    private val nullType = LiteralType("null", null)
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test
    fun parse() {
        val input = Input("true false null")

        Assert.assertTrue(trueType.canParse(input))
        Assert.assertEquals(true, trueType.parse(input, parser))

        input.increment()

        Assert.assertTrue(falseType.canParse(input))
        Assert.assertEquals(false, falseType.parse(input, parser))

        input.increment()

        Assert.assertTrue(nullType.canParse(input))
        Assert.assertNull(nullType.parse(input, parser))
    }
}