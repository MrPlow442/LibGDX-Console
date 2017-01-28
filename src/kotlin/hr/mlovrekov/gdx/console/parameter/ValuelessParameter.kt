package hr.mlovrekov.gdx.console.parameter

class ValuelessParameter(override val key: String) : Parameter<Any> {
    override val value: Any
        get() = throw UnsupportedOperationException()

    override fun hasValue() = false
}