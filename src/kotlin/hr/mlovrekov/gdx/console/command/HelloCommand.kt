package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ValueParameterDefinition
import hr.mlovrekov.gdx.console.token.type.MapType

class HelloCommand : ConsoleCommand {
    private val messageParamsParameterDefinition = ValueParameterDefinition(key = "messageParams",
                                                                            description = "map containing message params",
                                                                            type = MapType::class.java)

    private val defaultGreeting = "Hello"
    private val defaultTarget = "World"

    override val name = "hello"
    override val description = "Hello world command for testing purposes"
    override val parameterDefinitions = Array(arrayOf(messageParamsParameterDefinition))

    override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {

        var greeting = defaultGreeting
        var target = defaultTarget
        parameters.ifPresent(messageParamsParameterDefinition) {
            val greetingValue = it["greeting"]
            if (greetingValue is String) {
                greeting = greetingValue
            }
            val targetValue = it["target"]
            if (targetValue is String) {
                target = targetValue
            }
        }

        console.log("test", "$greeting $target")
    }
}