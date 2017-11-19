package hr.mlovrekov.gdx.console.util

import org.junit.Test

import org.junit.Assert.*

class ConstrainedQueueTest {

    @Test
    fun addFirst() {
        val queue = ConstrainedQueue<Int>(2)

        queue.addFirst(1)
        queue.addFirst(2)
        queue.addFirst(3)

        assertEquals("[3, 2]", queue.toString())
    }

    @Test
    fun addLast() {
        val queue = ConstrainedQueue<Int>(2)

        queue.addLast(1)
        queue.addLast(2)
        queue.addLast(3)

        assertEquals("[2, 3]", queue.toString())
    }

    @Test
    fun addFirstReturning() {
        val queue = ConstrainedQueue<Int>(2)

        val firstResult = queue.addFirstReturning(1)
        val secondResult = queue.addFirstReturning(2)
        val thirdResult = queue.addFirstReturning(3)

        assertEquals("[3, 2]", queue.toString())
        assertEquals(null, firstResult)
        assertEquals(null, secondResult)
        assertEquals(1, thirdResult)
    }

    @Test
    fun addLastReturning() {
        val queue = ConstrainedQueue<Int>(2)

        val firstResult = queue.addLastReturning(1)
        val secondResult = queue.addLastReturning(2)
        val thirdResult = queue.addLastReturning(3)

        assertEquals("[2, 3]", queue.toString())
        assertEquals(null, firstResult)
        assertEquals(null, secondResult)
        assertEquals(1, thirdResult)
    }

}