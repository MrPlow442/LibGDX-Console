package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ValueParameterDefinition
import hr.mlovrekov.gdx.console.token.type.StringType

class LogCommand: ConsoleCommand() {
    private val messageTypeParameterDefinition = MessageTypeParameterDefinition()
    private val logTypeParameterDefinition = LogTypeParameterDefinition()

    override val name = "log"
    override val description = "logs entered text to console"
    override val parameters = Array(arrayOf(messageTypeParameterDefinition, logTypeParameterDefinition))

    override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {
        parameters.ifPresent(messageTypeParameterDefinition) {
            val logType = parameters.get(logTypeParameterDefinition)
            when (logType) {
                "ERROR" -> console.error("log", it)
                "DEBUG" -> console.debug("log", it)
                else    -> console.log("log", it)
            }
        }
    }

    class LogTypeParameterDefinition: ValueParameterDefinition<StringType, String> {
        override val key = "type"
        override val description: String = "Log type. Permitted values are INFO(default), ERROR, DEBUG"
        override val type = StringType::class.java
    }

    class MessageTypeParameterDefinition: ValueParameterDefinition<StringType, String> {
        override val key = "message"
        override val description = "Log message"
        override val type = StringType::class.java

    }
}