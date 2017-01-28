package hr.mlovrekov.gdx.console.parameter

class ValueParameter<out T : Any?>(override val key: String, override val value: T) : Parameter<T> {
    override fun hasValue() = true
}