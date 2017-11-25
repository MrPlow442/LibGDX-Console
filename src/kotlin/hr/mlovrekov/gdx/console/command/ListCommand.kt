package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters

class ListCommand : ConsoleCommand {
    private val inlineParameterDefinition = ParameterDefinition(key = "inline",
                                                                description = "Inline commands")

    override val name: String = "list"
    override val description: String = "Lists available commands"
    override val parameterDefinitions = Array(arrayOf(inlineParameterDefinition))

    override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {
        if (parameters.has(inlineParameterDefinition)) {
            console.print(commands.joinToString(", ") { it.name })
        } else {
            commands.forEach {
                console.print("${it.name} - ${it.description}")
            }
        }
    }
}