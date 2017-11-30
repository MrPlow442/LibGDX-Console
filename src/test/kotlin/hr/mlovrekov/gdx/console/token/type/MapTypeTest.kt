package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapTypeTest {

    private val mapType = MapType()
    private val parser = TokenConsoleParser(commands = arrayListOf(),
                                            types = arrayOf(StringType(),
                                                            LiteralType("true", true),
                                                            LiteralType("false", false),
                                                            LiteralType("null", null),
                                                            ColorType(),
                                                            IntegerType(),
                                                            FloatType(),
                                                            DoubleType(),
                                                            ArrayType(),
                                                            mapType))

    @Test
    fun parse() {
        val input = Input("{first: 1, \"second\": 2.0, third: 3.0d} {\"Hello World!\":\"Goodnight World!\", 1:first, color:#FF00FF}")

        assertTrue(mapType.canParse(input))

        val firstMap = mapType.parse(input, parser)

        assertEquals(1, firstMap.get("first"))
        assertEquals(2.0f, firstMap.get("second") as Float, 0.05f)
        assertEquals(3.0, firstMap.get("third") as Double, 0.05)

        input.increment()

        assertTrue(mapType.canParse(input))

        val secondMap = mapType.parse(input, parser)

        assertEquals("Goodnight World!", secondMap.get("Hello World!") as String)
        assertEquals("first", secondMap.get(1) as String)
        assertEquals(Color.valueOf("#FF00FF"), secondMap.get("color") as Color)
    }

}