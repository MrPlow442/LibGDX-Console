package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters

class ListCommand : ConsoleCommand() {
    private val inlineParameterDefinition = InlineParameterDefinition()

    override val name: String = "list"
    override val description: String = "Lists available commands"
    override val parameters = Array(arrayOf(inlineParameterDefinition))

    override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {
        if (parameters.has(inlineParameterDefinition)) {
            console.print(commands.joinToString(", ") { it.name })
        } else {
            commands.forEach {
                console.print("${it.name} - ${it.description}")
            }
        }
    }

    class InlineParameterDefinition : ParameterDefinition {
        override val key: String = "inline"
        override val description: String = "Inline commands"
    }
}