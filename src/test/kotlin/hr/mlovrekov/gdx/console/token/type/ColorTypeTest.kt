package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.ParseException
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class ColorTypeTest {
    private val type = ColorType()
    private val parser = Mockito.mock(TokenConsoleParser::class.java)!!

    @Test(expected = ParseException::class)
    fun parse() {
        val input = Input("#FF00FF #FFFFFFFF #ASDFGH")

        Assert.assertTrue(type.canParse(input))
        Assert.assertEquals(Color.valueOf("#FF00FF"), type.parse(input, parser))

        input.increment()

        Assert.assertTrue(type.canParse(input))
        Assert.assertEquals(Color.valueOf("#FFFFFFFF"), type.parse(input, parser))

        input.increment()

        Assert.assertTrue(type.canParse(input))
        type.parse(input, parser)
    }
}