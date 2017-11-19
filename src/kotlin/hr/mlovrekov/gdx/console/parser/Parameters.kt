package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.token.type.Type
import com.badlogic.gdx.utils.Array as GdxArray

class Parameters {
    private val paramsWithValue = ObjectMap<String, Any?>()
    private val params = GdxArray<String>()

    fun <T : Any?> put(key: String, value: T) {
        paramsWithValue.put(key, value)
    }

    fun add(key: String) = params.add(key)

    fun has(parameterDefinition: ParameterDefinition) = params.contains(parameterDefinition.key, false) || paramsWithValue.containsKey(parameterDefinition.key)

    fun hasValue(parameterDefinition: ParameterDefinition) = paramsWithValue.containsKey(parameterDefinition.key)

    @Suppress("UNCHECKED_CAST")
    fun <T: Type<U>, U> get(parameterDefinition: ValueParameterDefinition<T, U>) = paramsWithValue[parameterDefinition.key] as U?

    fun <T: Type<U>, U> ifPresent(parameterDefinition: ValueParameterDefinition<T, U>, consumer: (U) -> Unit) {
        val value = get(parameterDefinition)
        if(value != null) {
            consumer.invoke(value)
        }
    }

}