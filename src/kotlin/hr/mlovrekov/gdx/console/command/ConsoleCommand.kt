package hr.mlovrekov.gdx.console.command

import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters
import com.badlogic.gdx.utils.Array as GdxArray

abstract class ConsoleCommand {
    abstract val description: String
    abstract val parameters: com.badlogic.gdx.utils.Array<out ParameterDefinition<*>>
    abstract fun execute(console: AbstractConsole, parameters: Parameters)
}