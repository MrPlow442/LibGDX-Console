package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ValueParameterDefinition
import hr.mlovrekov.gdx.console.token.type.StringType

class LogCommand : ConsoleCommand {
    private val messageTypeParameterDefinition = ValueParameterDefinition(key = "value",
                                                                          description = "Log message",
                                                                          type = StringType::class.java)
    private val logTypeParameterDefinition = ValueParameterDefinition(key = "type",
                                                                      description = "Log type. Permitted values are INFO(default), ERROR, DEBUG",
                                                                      type = StringType::class.java)

    override val name = "log"
    override val description = "logs entered text to console"
    override val parameterDefinitions = Array(arrayOf(messageTypeParameterDefinition, logTypeParameterDefinition))

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

}