package hr.mlovrekov.gdx.console.parser

class ParameterDefinition<T : Any>(val keys: Array<String>,
                                   val type: Class<T>,
                                   val description: String = "")