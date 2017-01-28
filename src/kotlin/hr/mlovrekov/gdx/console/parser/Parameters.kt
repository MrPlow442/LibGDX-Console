package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Array as GdxArray

class Parameters {
    private val paramsWithValue = ObjectMap<Array<out String>, Any?>()
    private val params = com.badlogic.gdx.utils.Array<String>()

    fun <T : Any?> put(value: T, vararg keys: String) {
        paramsWithValue.put(keys, value)
    }

    fun add(vararg keys: String) = keys.forEach { params.add(it) }

    operator fun contains(key: String) = params.any { key in it } || paramsWithValue.any { key in it.key }

    fun has(key: String) = paramsWithValue.any { key in it.key }

    fun has(key: String, type: Class<*>) = paramsWithValue.any {
        key in it.key && it.value?.javaClass?.isAssignableFrom(type) ?: false
    }

    fun has(parameterDefinition: ParameterDefinition<*>): Boolean {
        if (parameterDefinition.type == Void::class.java) {
            return params.any { it in parameterDefinition.keys }
        } else {
            for (entry in paramsWithValue) {
                if (entry.key.any { it in parameterDefinition.keys }) {
                    return parameterDefinition.type == entry.value?.javaClass
                }
                continue
            }
            return false
        }
    }

    fun get(key: String) = paramsWithValue.find { key in it.key }!!.value

    fun get(parameterDefinition: ParameterDefinition<*>) = paramsWithValue.find { it.key.any { it in parameterDefinition.keys } }!!.value
}