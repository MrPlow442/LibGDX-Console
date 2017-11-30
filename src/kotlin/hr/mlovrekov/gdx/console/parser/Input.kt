package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.math.MathUtils

interface InspectableInput {
    val index: Int
    fun remaining(): Int
    fun isEmpty(): Boolean
    fun isEol(index: Int): Boolean
    fun isEol(): Boolean
    fun isBol(index: Int): Boolean
    fun isBol(): Boolean
    fun hasNext(count: Int): Boolean
    fun hasNext(): Boolean
    fun hasPrevious(count: Int): Boolean
    fun hasPrevious(): Boolean
    fun peek(index: Int): Char
    fun peek(): Char
    fun peekNext(): Char
    fun peekPrevious(): Char
    fun isChar(index: Int, char: Char, ignoreWhitespace: Boolean = false): Boolean
    fun isAtChar(char: Char, ignoreWhitespace: Boolean = false): Boolean
    fun isAtDigit(ignoreWhitespace: Boolean = false): Boolean
    fun isDigit(index: Int, ignoreWhitespace: Boolean = false): Boolean
    fun isAtLetter(ignoreWhitespace: Boolean = false): Boolean
    fun isLetter(index: Int, ignoreWhitespace: Boolean = false): Boolean
    fun isAtLetterOrDigit(ignoreWhitespace: Boolean = false): Boolean
    fun isLetterOrDigit(index: Int, ignoreWhitespace: Boolean = false): Boolean
    fun isAtWhitespace(): Boolean
    fun isWhitespace(index: Int): Boolean
    fun isOneOf(index: Int, vararg chars: Char, ignoreWhitespace: Boolean = false): Boolean
    fun isAtOneOf(vararg chars: Char, ignoreWhitespace: Boolean = false): Boolean
    fun nextIsChar(char: Char, ignoreWhitespace: Boolean = false): Boolean
    fun nextIsDigit(ignoreWhitespace: Boolean = false): Boolean
    fun nextIsLetter(ignoreWhitespace: Boolean = false): Boolean
    fun nextIsLetterOrDigit(ignoreWhitespace: Boolean = false): Boolean
    fun nextIsWhitespace(): Boolean
    fun previousIsChar(char: Char, ignoreWhitespace: Boolean = false): Boolean
    fun previousIsDigit(ignoreWhitespace: Boolean = false): Boolean
    fun previousIsLetter(ignoreWhitespace: Boolean = false): Boolean
    fun previousIsLetterOrDigit(ignoreWhitespace: Boolean = false): Boolean
    fun previousIsWhitespace(): Boolean
    fun indexOf(char: Char): Int
    fun previousIndexOf(char: Char): Int
    fun indexOfWhitespace(): Int
    fun previousIndexOfWhitespace(): Int
    fun matches(string: String): Boolean
    fun matchesPrevious(string: String): Boolean
}

interface TraversableInput : InspectableInput {
    fun save()
    fun rollback()
    fun increment(step: Int = 1): Int
    fun increment(): Int
    fun getAndIncrement(step: Int = 1): Char
    fun getAndIncrement(): Char
    fun decrement(step: Int = 1): Int
    fun decrement(): Int
    fun getAndDecrement(step: Int = 1): Char
    fun getAndDecrement(): Char
    fun grabNext(count: Int): String
    fun grabAllNext(): String
    fun grabNextUntil(char: Char): String
    fun grabNextUntilWhitespace(): String
    fun grabPrevious(count: Int): String
    fun grabAllPrevious(): String
    fun grabPreviousUntil(char: Char): String
    fun grabPreviousUntilWhitespace(): String
    fun skipWhitespace()
}

class Input(private val input: String) : TraversableInput {
    init {
        input.trim()
    }

    override var index = 0
        private set

    private var rollbackIndex = -1

    override fun save() {
        rollbackIndex = index
    }

    override fun rollback() {
        if (rollbackIndex == -1) {
            return
        }
        index = rollbackIndex
        rollbackIndex = -1
    }

    override fun remaining() = input.lastIndex - index

    override fun isEmpty() = input.isEmpty()

    override fun isEol(index: Int) = index >= input.length

    override fun isEol() = isEol(index)

    override fun isBol(index: Int) = index <= -1

    override fun isBol() = isBol(index)

    override fun hasNext(count: Int) = (index + count) <= input.length

    override fun hasNext() = hasNext(1)

    override fun hasPrevious(count: Int) = (index - count) >= -1

    override fun hasPrevious() = hasPrevious(1)

    override fun peekNext() = peek(index + 1)

    override fun peek(index: Int) = input[index]

    override fun peek() = peek(index)

    override fun peekPrevious() = peek(index - 1)

    override fun isChar(index: Int, char: Char, ignoreWhitespace: Boolean): Boolean {
        var indexVar = index
        if(ignoreWhitespace) {
            while(!isBol(indexVar) && !isEol(indexVar) && isWhitespace(indexVar)) {
                ++indexVar
            }
        }

        return !isEol(indexVar) && peek(indexVar) == char
    }

    override fun isAtChar(char: Char, ignoreWhitespace: Boolean) = isChar(index, char, ignoreWhitespace)

    override fun isDigit(index: Int, ignoreWhitespace: Boolean): Boolean {
        var indexVar = index
        if(ignoreWhitespace) {
            while(!isBol(indexVar) && !isEol(indexVar) && isWhitespace(indexVar)) {
                ++indexVar
            }
        }

        return !isEol(indexVar) && !isBol(indexVar) && peek(indexVar).isDigit()
    }

    override fun isAtDigit(ignoreWhitespace: Boolean) = isDigit(index, ignoreWhitespace)

    override fun isLetter(index: Int, ignoreWhitespace: Boolean): Boolean {
        var indexVar = index
        if(ignoreWhitespace) {
            while(!isBol(indexVar) && !isEol(indexVar) && isWhitespace(indexVar)) {
                ++indexVar
            }
        }

        return !isEol(indexVar) && !isBol(indexVar) && peek(indexVar).isLetter()
    }

    override fun isAtLetter(ignoreWhitespace: Boolean) = isLetter(index, ignoreWhitespace)

    override fun isLetterOrDigit(index: Int, ignoreWhitespace: Boolean): Boolean {
        var indexVar = index
        if(ignoreWhitespace) {
            while(!isBol(indexVar) && !isEol(indexVar) && isWhitespace(indexVar)) {
                ++indexVar
            }
        }

        return !isEol(indexVar) && !isBol(indexVar) && peek(indexVar).isLetterOrDigit()
    }

    override fun isAtLetterOrDigit(ignoreWhitespace: Boolean) = isLetterOrDigit(index, ignoreWhitespace)

    override fun isWhitespace(index: Int) = !isEol(index) && !isBol(index) && peek(index).isWhitespace()

    override fun isAtWhitespace() = isWhitespace(index)

    override fun isOneOf(index: Int, vararg chars: Char, ignoreWhitespace: Boolean): Boolean {
        var indexVar = index
        if(ignoreWhitespace) {
            while(!isBol(indexVar) && !isEol(indexVar) && isWhitespace(indexVar)) {
                ++indexVar
            }
        }
        return chars.any { peek(indexVar) == it }
    }

    override fun isAtOneOf(vararg chars: Char, ignoreWhitespace: Boolean) = isOneOf(index, *chars, ignoreWhitespace = ignoreWhitespace)

    override fun nextIsChar(char: Char, ignoreWhitespace: Boolean) = isChar(index + 1, char, ignoreWhitespace)

    override fun nextIsDigit(ignoreWhitespace: Boolean) = isDigit(index + 1, ignoreWhitespace)

    override fun nextIsLetter(ignoreWhitespace: Boolean) = isLetter(index + 1, ignoreWhitespace)

    override fun nextIsLetterOrDigit(ignoreWhitespace: Boolean) = isLetterOrDigit(index + 1, ignoreWhitespace)

    override fun nextIsWhitespace() = isWhitespace(index + 1)

    override fun previousIsChar(char: Char, ignoreWhitespace: Boolean) = isChar(index - 1, char, ignoreWhitespace)

    override fun previousIsDigit(ignoreWhitespace: Boolean) = isDigit(index - 1, ignoreWhitespace)

    override fun previousIsLetter(ignoreWhitespace: Boolean) = isLetter(index - 1, ignoreWhitespace)

    override fun previousIsLetterOrDigit(ignoreWhitespace: Boolean) = isLetterOrDigit(index - 1, ignoreWhitespace)

    override fun previousIsWhitespace() = isWhitespace(index - 1)

    override fun indexOf(char: Char) = input.indexOf(char, index)

    override fun previousIndexOf(char: Char) = input.lastIndexOf(char, index)

    override fun indexOfWhitespace(): Int {
        var index = index
        while(!isEol(index)) {
            if(input[index].isWhitespace()) {
                return index
            }
            index += 1
        }
        return -1
    }

    override fun previousIndexOfWhitespace(): Int {
        var index = index
        while(index != -1) {
            if(input[index].isWhitespace()) {
                return index
            }
            index -= 1
        }
        return -1
    }

    override fun matches(string: String) = input.regionMatches(index, string, 0, string.length)

    override fun matchesPrevious(string: String) = input.regionMatches(MathUtils.clamp(index - string.length, 0, input.length), string, 0, string.length)

    override fun increment(step: Int): Int {
        index = MathUtils.clamp(index + step, 0, input.length)
        return index
    }

    override fun increment(): Int = increment(1)

    override fun getAndIncrement(step: Int): Char {
        val current = peek()
        increment(step)
        return current
    }

    override fun getAndIncrement() = getAndIncrement(1)

    override fun decrement(step: Int): Int {
        index = MathUtils.clamp(index - step, -1, input.length)
        return index
    }

    override fun decrement() = decrement(1)

    override fun getAndDecrement(step: Int): Char {
        val current = peek()
        decrement(step)
        return current
    }

    override fun getAndDecrement() = getAndDecrement(1)

    override fun grabNext(count: Int): String {
        val end = MathUtils.clamp(index + count, 0, input.length)
        val text = input.substring(index, end)
        increment(end - index)
        return text
    }

    override fun grabAllNext() = grabNext(input.length - index)

    override fun grabNextUntil(char: Char): String {
        val end = indexOf(char)
        if(end == -1) {
            return grabAllNext()
        }
        return grabNext(end - index)
    }

    override fun grabNextUntilWhitespace(): String {
        val end = indexOfWhitespace()
        if(end == -1) {
            return grabAllNext()
        }
        return grabNext(end - index)
    }

    override fun grabPrevious(count: Int): String {
        val end = index + 1
        val start = MathUtils.clamp(end - count, 0, index)
        val text = input.substring(start, end)
        decrement(end - start)
        return text
    }

    override fun grabAllPrevious() = grabPrevious(index + 1)

    override fun grabPreviousUntil(char: Char): String {
        val end = previousIndexOf(char)
        if(end == -1) {
            return grabAllPrevious()
        }
        return grabPrevious(index - end)
    }

    override fun grabPreviousUntilWhitespace(): String {
        val end = previousIndexOfWhitespace()
        if(end == -1) {
            return grabAllPrevious()
        }
        return grabPrevious(index - end)
    }

    override fun skipWhitespace() {
        while (peek().isWhitespace()) {
            increment()
        }
    }
}