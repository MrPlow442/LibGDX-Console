package hr.mlovrekov.gdx.console.util

import com.badlogic.gdx.utils.Queue

class ConstrainedQueue<T> : Queue<T> {
    constructor(maxSize: Int) : super(maxSize)
    constructor(maxSize: Int, type: Class<T>) : super(maxSize, type)

    fun addFirstReturning(obj: T): T? {
        var evicted: T? = null
        if(this.values.size == size) {
            evicted = this.removeLast()
        }
        super.addFirst(obj)
        return evicted
    }

    override fun addFirst(obj: T) {
        addFirstReturning(obj)
    }

    fun addLastReturning(obj: T): T? {
        var evicted: T? = null
        if(this.values.size == size) {
            evicted = this.removeFirst()
        }
        super.addLast(obj)
        return evicted
    }

    override fun addLast(obj: T) {
        addLastReturning(obj)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun isEmpty() = size == 0
}