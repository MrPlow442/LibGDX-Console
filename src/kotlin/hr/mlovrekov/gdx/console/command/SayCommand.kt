package hr.mlovrekov.gdx.console.command

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.parser.ParameterDefinition
import hr.mlovrekov.gdx.console.parser.Parameters
import java.util.*

class SayCommand : ConsoleCommand() {
    private val valueParameterDefinition = ParameterDefinition(arrayOf("-v", "--value"),
                                                               String::class.java,
                                                               "String to output")
    private val colorParameterDefinition = ParameterDefinition(arrayOf("-c", "--color"),
                                                               Color::class.java,
                                                               "Color of output")

    override val description: String = "Outputs input value"
    override val parameters = Array(arrayOf(valueParameterDefinition, colorParameterDefinition))

    override fun execute(console: AbstractConsole, parameters: Parameters) {
        if (!parameters.has(valueParameterDefinition)) {
            console.error("Error", "Value parameter ${Arrays.toString(valueParameterDefinition.keys)} not provided")
            return
        }

        var output = parameters.get(valueParameterDefinition) as String

        if (parameters.has(colorParameterDefinition)) {
            val color = parameters.get(colorParameterDefinition) as Color
            output = "[#$color]$output"
        }

        console.printLine(output)
    }
}