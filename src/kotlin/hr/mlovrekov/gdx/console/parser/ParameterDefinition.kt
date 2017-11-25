package hr.mlovrekov.gdx.console.parser

import hr.mlovrekov.gdx.console.token.type.Type

open class ParameterDefinition(val key: String, val description: String)

open class ValueParameterDefinition<T : Type<U>, out U>(key: String,
                                                        description: String,
                                                        val type: Class<T>) : ParameterDefinition(key, description) {
    companion object {
        const val PRIMARY_KEY = "value"
    }
}