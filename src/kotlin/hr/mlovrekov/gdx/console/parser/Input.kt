package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.math.MathUtils

class Input(val input: String) {
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

    fun isEmpty() = input.isEmpty()

    fun isEol(index: Int) = index >= input.length

    fun isEol() = isEol(index)

    fun isFirst(index: Int) = index == 0

    fun isFirst() = isFirst(index)

    fun hasNext(step: Int) = !isEol(index + step)

    fun hasNext() = hasNext(1)

    fun hasPrevious(step: Int) = !isFirst(index - step)

    fun hasPrevious() = hasPrevious(1)

    fun peekNext() = input[index + 1]

    fun peek() = input[index]

    fun peekPrevious() = input[index - 1]

    fun isAtChar(char: Char) = peek() == char

    fun isAtDigit() = peek().isDigit()

    fun isAtLetter() = peek().isLetter()

    fun isAtLetterOrDigit() = peek().isLetterOrDigit()

    fun isAtWhitespace() = peek().isWhitespace()

    fun nextIsAtChar(char: Char) = hasNext() && peekNext() == char

    fun nextIsAtDigit() = hasNext() && peekNext().isDigit()

    fun nextIsAtLetter() = hasNext() && peekNext().isLetter()

    fun nextIsAtLetterOrDigit() = hasNext() && peekNext().isLetterOrDigit()

    fun nextIsAtWhitespace() = hasNext() && peekNext().isWhitespace()

    fun previousIsAtChar(char: Char) = hasPrevious() && peekPrevious() == char

    fun previousIsAtDigit() = hasPrevious() && peekPrevious().isDigit()

    fun previousIsAtLetter() = hasPrevious() && peekPrevious().isLetter()

    fun previousIsAtLetterOrDigit() = hasPrevious() && peekPrevious().isLetterOrDigit()

    fun previousIsAtWhitespace() = hasPrevious() && peekPrevious().isWhitespace()

    fun matchesLiteral(literal: String) = input.regionMatches(index, literal, 0, literal.length)

    fun increment(step: Int): Int {
        index += step
        return index
    }

    fun increment() = increment(1)

    fun getAndIncrement(step: Int): Char {
        val current = peek()
        increment(step)
        return current
    }

    fun getAndIncrement(): Char = getAndIncrement(1)

    fun decrement(step: Int): Int {
        index -= step
        return index
    }

    fun decrement() = decrement(1)

    fun getAndDecrement(step: Int): Char {
        val current = peek()
        decrement(step)
        return current
    }

    fun getAndDecrement() = getAndDecrement(1)

    fun grabNext(count: Int): String {
        val end = MathUtils.clamp(index + count, 0, input.length)
        val text = input.substring(index, end)
        increment(end - index)
        return text
    }

    fun grabAllNext(): String {
        return grabNext(input.length - index)
    }

    fun grabPrevious(count: Int): String {
        val start = MathUtils.clamp(index - count, 0, index - 1)
        val text = input.substring(start, index)
        decrement(index - start)
        return text
    }

    fun grabAllPrevious(): String {
        return grabPrevious(index)
    }

    fun skipWhitespace() {
        while (peek().isWhitespace()) {
            increment()
        }
    }
}