package hr.mlovrekov.gdx.console.parser

import org.junit.Test

import org.junit.Assert.*

class InputTest {

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
    fun rollback() {
        val input = Input("1234567890")

        input.increment(2)

        input.begin()
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

    @Test
    fun indexOf() {
        val input = Input("1234567890")

        assertEquals(4, input.indexOf('5'))
    }

    @Test
    fun lastIndexOf() {
        val input = Input("0987654321")

        input.increment(input.remaining())

        assertEquals(5, input.previousIndexOf('5'))
    }

    @Test
    fun matches() {
        val input = Input("Hello world!")

        input.increment(6)

        assertTrue(input.matches("world!"))
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