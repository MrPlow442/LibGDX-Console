package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters

class HelpCommand : ConsoleCommand() {
    private val commandNameParameterDefinition = ParameterDefinition(arrayOf("-c", "--command"),
                                                                     String::class.java,
                                                                     "Command name for which to show the help screen for")

    override val description: String = "Shows help for a specific command or this description if none specified"
    override val parameters = Array(arrayOf(commandNameParameterDefinition))

    override fun execute(console: AbstractConsole, parameters: Parameters) {
        if (parameters.has(commandNameParameterDefinition)) {
            val commandNameValue = parameters.get(commandNameParameterDefinition) as String
            if (!console.commands.containsKey(commandNameValue)) {
                console.error("Error", "Command $commandNameValue doesn't exist")
                return
            }
            val command = console.commands[commandNameValue]
            printHelpForCommand(console, commandNameValue, command)
        } else {
            val helpCommandName = console.commands.findKey(this, true)
            val command = console.commands[helpCommandName]
            printHelpForCommand(console, helpCommandName, command)
        }
    }

    private fun printHelpForCommand(console: AbstractConsole, commandName: String, command: ConsoleCommand) {
        console.printLine("Command: $commandName")
        console.printLine("Description: ${command.description}")

        if (command.parameters.size > 0) {
            console.printLine("Parameters:")
            for (parameterDef in command.parameters) {
                console.printLine("  Keys: ${parameterDef.keys.joinToString(", ")}")
                if (parameterDef.type != Void::class.java) {
                    console.printLine("  Value type: ${parameterDef.type.name}")
                }
                console.printLine("  Description: ${parameterDef.description}")
            }
        }
    }

}