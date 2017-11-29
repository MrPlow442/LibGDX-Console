package hr.mlovrekov.gdx.console.parser

import org.junit.Assert.*
import org.junit.Test

class InputTest {

    @Test
    fun isEmpty() {
        val emptyInput = Input("")
        val input = Input("1234567890")

        assertTrue(emptyInput.isEmpty())
        assertFalse(input.isEmpty())
    }

    @Test
    fun increment() {
        val input = Input("1234567890")

        assertEquals(0, input.index)
        input.increment(15)
        assertEquals(10, input.index)
    }

    @Test
    fun decrement() {
        val input = Input("1234567890")

        input.increment(9)
        assertEquals(9, input.index)

        input.decrement(15)
        assertEquals(-1, input.index)
    }

    @Test
    fun isEol() {
        val input = Input("1")

        assertFalse(input.isEol())
        assertTrue(input.isEol(1))

        input.increment()

        assertTrue(input.isEol())
    }

    @Test
    fun isBol() {
        val input = Input("1")

        assertFalse(input.isBol())
        assertTrue(input.isBol(-1))

        input.decrement()

        assertTrue(input.isBol())
    }

    @Test
    fun remaining() {
        val input = Input("1234567890")

        assertEquals(input.remaining(), 9)

        input.increment(5)

        assertEquals(input.remaining(), 4)
    }

    @Test
    fun rollback() {
        val input = Input("1234567890")

        input.increment(2)

        input.save()
        input.increment(5)
        input.rollback()

        assertEquals(2, input.index)
    }

    @Test
    fun hasNext() {
        val input = Input("1234567890")

        val resultBuilder = StringBuilder()
        while(input.hasNext()) {
            resultBuilder.append(input.getAndIncrement())
        }

        assertEquals("1234567890", resultBuilder.toString())
    }

    @Test
    fun hasPrevious() {
        val input = Input("1234567890")

        input.increment(input.remaining())

        val resultBuilder = StringBuilder()
        while(input.hasPrevious()) {
            resultBuilder.append(input.getAndDecrement())
        }

        assertEquals("0987654321", resultBuilder.toString())
    }

    @Test(expected = StringIndexOutOfBoundsException::class)
    fun peek() {
        val input = Input("1234567890")

        assertEquals(input.peek(), '1')

        input.decrement()
        input.peek()
    }

    @Test
    fun isAtChar() {
        val input = Input("Hello     world")

        assertTrue(input.isAtChar('H'))
        assertTrue(input.isChar(4, 'o'))
        assertFalse(input.isChar(5, 'w'))
        assertTrue(input.isChar(5, 'w', true))
    }

    @Test
    fun isAtDigit() {
        val input = Input("H3llo     666")

        assertFalse(input.isAtDigit())
        assertTrue(input.isDigit(1))
        assertFalse(input.isDigit(5))
        assertTrue(input.isDigit(5, true))
    }

    @Test
    fun isAtLetter() {
        val input = Input("H3llo     six66")

        assertTrue(input.isAtLetter())
        assertFalse(input.nextIsLetter())
        assertFalse(input.isLetter(5))
        assertTrue(input.isLetter(5, true))
    }

    @Test
    fun isAtLetterOrDigit() {
        val input = Input("a2_    asd")

        assertTrue(input.isAtLetterOrDigit())
        assertTrue(input.nextIsLetterOrDigit())
        assertFalse(input.isLetterOrDigit(2))
        assertFalse(input.isLetterOrDigit(3))
        assertTrue(input.isLetterOrDigit(3, true))
    }

    @Test
    fun isAtWhitespace() {
        val input = Input("H ello world!")

        assertFalse(input.isAtWhitespace())
        assertTrue(input.nextIsWhitespace())
        assertFalse(input.isWhitespace(5))
        assertTrue(input.isWhitespace(6))
    }

    @Test
    fun indexOf() {
        val input = Input("12345 67890")

        assertEquals(4, input.indexOf('5'))
        assertEquals(5, input.indexOfWhitespace())
    }

    @Test
    fun lastIndexOf() {
        val input = Input("09876 54321")

        input.increment(input.remaining())

        assertEquals(6, input.previousIndexOf('5'))
        assertEquals(5, input.previousIndexOfWhitespace())
    }

    @Test
    fun matches() {
        val input = Input("Hello world!")

        input.increment(6)

        assertTrue(input.matches("world!"))
    }

    @Test
    fun matchesPrevious() {
        val input = Input("Hello world!")

        input.increment(5)

        assertTrue(input.matchesPrevious("Hello"))
    }

    @Test
    fun grabNext() {
        val input = Input("Hello world!")

        input.increment(6)

        val result = input.grabNext(5)

        assertEquals("world", result)
    }

    @Test
    fun grabAllNext() {
        val input = Input("Hello world!")

        input.increment(6)

        val result = input.grabAllNext()

        assertEquals("world!", result)
    }

    @Test
    fun grabNextUntil() {
        val input = Input("Hello world!")

        val result = input.grabNextUntil('o')

        assertEquals("Hell", result)
    }

    @Test
    fun grabNextUntilWhitespace() {
        val input = Input("Hello world!")

        val result = input.grabNextUntilWhitespace()

        assertEquals("Hello", result)
    }

    @Test
    fun grabPrevious() {
        val input = Input("Hello world!")

        input.increment(4)

        val result = input.grabPrevious(5)

        assertEquals("Hello", result)
    }

    @Test
    fun grabAllPrevious() {
        val input = Input("Hello world!")

        input.increment(10)

        val result = input.grabAllPrevious()

        assertEquals("Hello world", result)
    }

    @Test
    fun grabPreviousUntil() {
        val input = Input("Hello world!")

        input.increment(4)

        val result = input.grabPreviousUntil('e')

        assertEquals("llo", result)
    }

    @Test
    fun grabPreviousUntilWhitespace() {
        val input = Input("Hello world!")

        input.increment(input.remaining())

        val result = input.grabPreviousUntilWhitespace()

        assertEquals("world!", result)
    }

    @Test
    fun skipWhitespace() {
        val input = Input("Hello        world!")

        input.increment(5)

        assertTrue(input.peek().isWhitespace())

        input.skipWhitespace()

        assertTrue(input.isAtChar('w'))
        assertEquals("world!", input.grabAllNext())
    }

}