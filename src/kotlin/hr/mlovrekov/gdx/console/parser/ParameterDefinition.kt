package hr.mlovrekov.gdx.console.parser

import hr.mlovrekov.gdx.console.token.type.Type

interface ParameterDefinition {
    val key: String
    val description: String
}

interface ValueParameterDefinition<T: Type<U>, U> : ParameterDefinition {
    val type: Class<T>
}