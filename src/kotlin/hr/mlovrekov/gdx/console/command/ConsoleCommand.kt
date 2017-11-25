package hr.mlovrekov.gdx.console.command

import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters
import hr.mlovrekov.gdx.console.parser.ValueParameterDefinition
import com.badlogic.gdx.utils.Array as GdxArray

interface ConsoleCommand {
    val name: String
    val description: String
    val parameterDefinitions: com.badlogic.gdx.utils.Array<out ParameterDefinition>
    fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters)
}