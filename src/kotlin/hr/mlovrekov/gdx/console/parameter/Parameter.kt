package hr.mlovrekov.gdx.console.parameter

interface Parameter<out T : Any?> {
    val key: String
    val value: T
    fun hasValue(): Boolean
}

