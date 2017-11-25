package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ValueParameterDefinition
import hr.mlovrekov.gdx.console.token.type.StringType

class HelpCommand : ConsoleCommand {
    private val commandNameParameterDefinition = ValueParameterDefinition(key = "value",
                                                                          description = "Command name for which to show the help screen for",
                                                                          type = StringType::class.java)

    override val name: String = "help"
    override val description: String = "Shows help for a specific command or this description if none specified"
    override val parameterDefinitions = Array(arrayOf(commandNameParameterDefinition))

    override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {
        if (parameters.hasValue(commandNameParameterDefinition)) {
            val commandNameValue = parameters.get(commandNameParameterDefinition) as String
            val command = commands.find { it.name == commandNameValue }
            if (command == null) {
                console.error("Error", "Command $commandNameValue doesn't exist")
                return
            }
            printHelpForCommand(console, command)
        } else {
            printHelpForCommand(console, this)
        }
    }

    private fun printHelpForCommand(console: AbstractConsole<*>, command: ConsoleCommand) {
        console.print("Command: ${command.name}")
        console.print("Description: ${command.description}")

        if (command.parameterDefinitions.size > 0) {
            console.print("Parameters:")
            for (parameterDef in command.parameterDefinitions) {
                console.print("  Name: ${parameterDef.key}")
                if (parameterDef is ValueParameterDefinition<*, *>) {
                    console.print("  Value type: ${parameterDef.type.simpleName}")
                }
                console.print("  Description: ${parameterDef.description}")
                console.print("--------------------------------------")
            }
        }
    }
}