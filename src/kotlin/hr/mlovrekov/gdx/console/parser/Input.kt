package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.math.MathUtils

@Suppress("NOTHING_TO_INLINE")
class Input(private val input: String) {
    init {
        input.trim()
    }

    var index = 0
        private set

    private var rollbackIndex = -1

    fun begin() {
        rollbackIndex = index
    }

    fun rollback() {
        if (rollbackIndex == -1) {
            return
        }
        index = rollbackIndex
        rollbackIndex = -1
    }

    fun remaining() = input.lastIndex - index

    fun isEmpty() = input.isEmpty()

    fun isEol(charIndex: Int = index) = charIndex >= input.length

    fun isBol(charIndex: Int = index) = charIndex <= -1

    fun hasNext(count: Int = 1) = (index + count) <= input.length

    fun hasPrevious(count: Int = 1) = (index - count) >= -1

    fun peekNext() = peek(index + 1)

    fun peek(peekIndex: Int = index) = input[peekIndex]

    fun peekPrevious() = peek(index - 1)

    inline fun isAtChar(char: Char) = !isEol() && peek() == char

    inline fun isAtDigit() = !isEol() && peek().isDigit()

    inline fun isAtLetter() = !isEol() && peek().isLetter()

    inline fun isAtLetterOrDigit() = !isEol() && peek().isLetterOrDigit()

    inline fun isAtWhitespace() = !isEol() && peek().isWhitespace()

    inline fun nextIsAtChar(char: Char) = hasNext() && peekNext() == char

    inline fun nextIsAtDigit() = hasNext() && peekNext().isDigit()

    inline fun nextIsAtLetter() = hasNext() && peekNext().isLetter()

    inline fun nextIsAtLetterOrDigit() = hasNext() && peekNext().isLetterOrDigit()

    inline fun nextIsAtWhitespace() = hasNext() && peekNext().isWhitespace()

    inline fun previousIsAtChar(char: Char) = hasPrevious() && peekPrevious() == char

    inline fun previousIsAtDigit() = hasPrevious() && peekPrevious().isDigit()

    inline fun previousIsAtLetter() = hasPrevious() && peekPrevious().isLetter()

    inline fun previousIsAtLetterOrDigit() = hasPrevious() && peekPrevious().isLetterOrDigit()

    inline fun previousIsAtWhitespace() = hasPrevious() && peekPrevious().isWhitespace()

    fun indexOf(char: Char) = input.indexOf(char, index)

    fun previousIndexOf(char: Char) = input.lastIndexOf(char, index)

    fun indexOfWhitespace(): Int {
        var index = index
        while(!isEol(index)) {
            if(input[index].isWhitespace()) {
                return index
            }
            index += 1
        }
        return -1
    }

    fun previousIndexOfWhitespace(): Int {
        var index = index
        while(index != -1) {
            if(input[index].isWhitespace()) {
                return index
            }
            index -= 1
        }
        return -1
    }

    fun matches(string: String) = input.regionMatches(index, string, 0, string.length)

    fun increment(step: Int = 1): Int {
        index = MathUtils.clamp(index + step, 0, input.length)
        return index
    }

    fun getAndIncrement(step: Int = 1): Char {
        val current = peek()
        increment(step)
        return current
    }

    fun decrement(step: Int = 1): Int {
        index = MathUtils.clamp(index - step, -1, input.length)
        return index
    }

    fun getAndDecrement(step: Int = 1): Char {
        val current = peek()
        decrement(step)
        return current
    }

    fun grabNext(count: Int): String {
        val end = MathUtils.clamp(index + count, 0, input.length)
        val text = input.substring(index, end)
        increment(end - index)
        return text
    }

    fun grabAllNext() = grabNext(input.length - index)

    fun grabNextUntil(char: Char): String {
        val end = indexOf(char)
        if(end == -1) {
            return grabAllNext()
        }
        return grabNext(end - index)
    }

    fun grabNextUntilWhitespace(): String {
        val end = indexOfWhitespace()
        if(end == -1) {
            return grabAllNext()
        }
        return grabNext(end - index)
    }

    fun grabPrevious(count: Int): String {
        val end = index + 1
        val start = MathUtils.clamp(end - count, 0, index)
        val text = input.substring(start, end)
        decrement(end - start)
        return text
    }

    fun grabAllPrevious() = grabPrevious(index + 1)

    fun grabPreviousUntil(char: Char): String {
        val end = previousIndexOf(char)
        if(end == -1) {
            return grabAllPrevious()
        }
        return grabPrevious(index - end)
    }

    fun grabPreviousUntilWhitespace(): String {
        val end = previousIndexOfWhitespace()
        if(end == -1) {
            return grabAllPrevious()
        }
        return grabPrevious(index - end)
    }

    fun skipWhitespace() {
        while (peek().isWhitespace()) {
            increment()
        }
    }
}