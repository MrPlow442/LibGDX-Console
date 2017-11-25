package hr.mlovrekov.gdx.console.parser

import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.token.CommandParser
import hr.mlovrekov.gdx.console.token.ParameterParser
import hr.mlovrekov.gdx.console.token.type.*
import com.badlogic.gdx.utils.Array as GdxArray

class TokenConsoleParser(private val commands: List<ConsoleCommand>,
                         private val types: Array<Type<*>> = DEFAULT_TYPES) : ConsoleParser {

    companion object {
        private val DEFAULT_TYPES = arrayOf(StringType(),
                                            LiteralType("true", true),
                                            LiteralType("false", false),
                                            LiteralType("null", null),
                                            ColorType(),
                                            NumberType(),
                                            ArrayType(),
                                            MapType())
    }

    private val commandParser = CommandParser()
    private val parameterParser = ParameterParser()

    override fun parse(line: String): ParseResult {
        if (line.isBlank()) {
            throw ParseException(0, "No input to parse")
        }

        val input = Input(line)

        if (!commandParser.canParse(input)) {
            throw ParseException(input.index, "Invalid symbol '${input.peek()}' in command name at column ${input.index + 1}")
        }

        val command = commandParser.parse(input, commands)

        val parameters = if (parameterParser.canParse(input)) {
            parameterParser.parse(input, command, this)
        } else {
            Parameters()
        }

        return ParseResult(command, parameters)
    }

    fun getType(clazz: Class<*>): Type<*>? = types.find { clazz.isInstance(it) }

    @Suppress("LoopToCallChain")
    fun parseToken(input: TraversableInput): Any? {
        input.save()
        for (type in types) {
            if (!type.canParse(input)) { continue }
            try {
                return type.parse(input, this)
            } catch (ex: ParseException) {
                input.rollback()
                if (types.indexOf(type) == types.lastIndex) {
                    throw ex
                }
            }
        }
        throw ParseException(input.index,
                             "Unknown symbol '${input.peek()}' at column ${input.index + 1}")
    }

}