package hr.mlovrekov.gdx.console.token.type

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array
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
                                                    IntegerType(),
                                                    FloatType(),
                                                    DoubleType(),
                                                    arrayType,
                                                    MapType()))

    @Test
    fun parseEmpty() {
        val input = Input("[]")

        assertTrue(arrayType.canParse(input))

        assertEquals(0, arrayType.parse(input, parser).size)
    }

    @Test
    fun parseMixed() {
        val input = Input("[1,2.0,3.0d, hello, \"Hello World!\", #FF00FFFF]")

        assertTrue(arrayType.canParse(input))

        val array = arrayType.parse(input, parser)

        assertEquals(1, array[0] as Int)
        assertEquals(2.0f, array[1] as Float, 0.05f)
        assertEquals(3.0, array[2] as Double, 0.05)
        assertEquals("hello", array[3] as String)
        assertEquals("Hello World!", array[4] as String)
        assertEquals(Color.valueOf("#FF00FFFF"), array[5] as Color)
    }

    @Test
    fun parseNested() {
        val input = Input("[[1,2,3],[hello,world],[#FF00FF,#00FF00]]")

        assertTrue(arrayType.canParse(input))

        val array = arrayType.parse(input, parser)

        val firstSubArray = array[0] as Array<Any?>

        assertEquals(1, firstSubArray[0] as Int)
        assertEquals(2, firstSubArray[1] as Int)
        assertEquals(3, firstSubArray[2] as Int)

        val secondSubArray = array[1] as Array<Any?>

        assertEquals("hello", secondSubArray[0] as String)
        assertEquals("world", secondSubArray[1] as String)

        val thirdSubArray = array[2] as Array<Any?>

        assertEquals(Color.valueOf("#FF00FF"), thirdSubArray[0] as Color)
        assertEquals(Color.valueOf("#00FF00"), thirdSubArray[1] as Color)
    }
}