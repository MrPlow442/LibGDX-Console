package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ObjectMap
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
    fun parseEmpty() {
        val input = Input("{}")

        assertTrue(mapType.canParse(input))

        assertEquals(0, mapType.parse(input, parser).size)
    }

    @Test
    fun parseMixed() {
        val input = Input("{first: 1, \"second\": 2.0, 3: 3.0d,\"Hello World!\":\"Goodnight World!\", 2.0d:#FF00FF}")

        assertTrue(mapType.canParse(input))

        val map = mapType.parse(input, parser)

        assertEquals(1, map.get("first"))
        assertEquals(2.0f, map.get("second") as Float, 0.05f)
        assertEquals(3.0, map.get(3) as Double, 0.05)
        assertEquals("Goodnight World!", map.get("Hello World!") as String)
        assertEquals(Color.valueOf("#FF00FF"), map.get(2.0) as Color)
    }

    @Test
    fun parseNested() {
        val input = Input("{first: {val1: 1, val2: 2}, second: {val1: 3, val2: 4}}")

        assertTrue(mapType.canParse(input))

        val map = mapType.parse(input, parser)

        val firstSubMap = map.get("first") as ObjectMap<Any, Any?>

        assertEquals(1, firstSubMap.get("val1"))
        assertEquals(2, firstSubMap.get("val2"))

        val secondSubMap = map.get("second") as ObjectMap<Any, Any?>

        assertEquals(3, secondSubMap.get("val1"))
        assertEquals(4, secondSubMap.get("val2"))
    }

}