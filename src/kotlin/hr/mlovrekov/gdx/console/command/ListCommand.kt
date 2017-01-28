package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters

class ListCommand : ConsoleCommand() {
    private val inlineParameterDefinition = ParameterDefinition(arrayOf("-i", "--inline"),
                                                                Void::class.java,
                                                                "Shows the commands inline")

    override val description: String = "Lists available commands"
    override val parameters = Array(arrayOf(inlineParameterDefinition))

    override fun execute(console: AbstractConsole, parameters: Parameters) {
        if (parameters.has(inlineParameterDefinition)) {
            console.printLine(console.commands.map { it.key }.joinToString(", "))
        } else {
            console.commands.forEach {
                console.printLine("${it.key} - ${it.value.description}")
            }
        }
    }
}