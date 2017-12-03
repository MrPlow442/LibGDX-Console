package hr.mlovrekov.gdx.console.parser

import com.badlogic.gdx.utils.Array
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.command.ConsoleCommand
import hr.mlovrekov.gdx.console.token.type.StringType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TokenConsoleParserTest {

    private val simpleTestCommand = SimpleTestCommand()
    private val simpleParameterTestCommand = SimpleParameterTestCommand()
    private val valueParameterTestCommand = ValueParameterTestCommand()
    private val implicitValueParameterTestCommand = ImplicitValueParameterTestCommand()

    private val parser = TokenConsoleParser(listOf(simpleTestCommand,
                                                   simpleParameterTestCommand,
                                                   valueParameterTestCommand,
                                                   implicitValueParameterTestCommand))


    @Test(expected = ParseException::class)
    fun parseEmptyLine() {
        parser.parse("")
    }

    @Test
    fun parseCommand() {
        val parseResult = parser.parse("test")

        assertEquals(simpleTestCommand, parseResult.command)
        assertEquals(0, parseResult.parameters.size())
    }

    @Test
    fun parseCommandWithSimpleParam() {
        val parseResult = parser.parse("test2 param")

        assertEquals(simpleParameterTestCommand, parseResult.command)
        assertEquals(1, parseResult.parameters.size())
        assertTrue(parseResult.parameters.has("param"))
    }

    @Test
    fun parseCommandWithValueParam() {
        val parseResult = parser.parse("test3 param=hello")

        assertEquals(valueParameterTestCommand, parseResult.command)
        assertEquals(1, parseResult.parameters.size())
        assertTrue(parseResult.parameters.has("param"))
        assertEquals("hello", parseResult.parameters.get("param", StringType::class.java))
    }

    @Test
    fun parseCommandWithImplicitValueParam() {
        val parseResult = parser.parse("test4 hello")

        assertEquals(implicitValueParameterTestCommand, parseResult.command)
        assertEquals(1, parseResult.parameters.size())
        assertTrue(parseResult.parameters.has("value"))
        assertEquals("hello", parseResult.parameters.get("value", StringType::class.java))
    }

    @Test(expected = ParseException::class)
    fun failWithUnknownCommand() {
        parser.parse("test5")
    }

    @Test(expected = ParseException::class)
    fun failWithUnknownParam() {
        parser.parse("test2 asdf")
    }

    class SimpleTestCommand : ConsoleCommand {
        override val name = "test"
        override val description = "Test command"
        override val parameterDefinitions = Array<ParameterDefinition>()

        override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {}
    }

    class SimpleParameterTestCommand : ConsoleCommand {
        private val simpleParam = ParameterDefinition(key = "param",
                                                      description = "test param")
        override val name = "test2"
        override val description = "Test command"
        override val parameterDefinitions = Array<ParameterDefinition>(arrayOf(simpleParam))

        override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {}
    }

    class ValueParameterTestCommand : ConsoleCommand {

        private val valueParam = ValueParameterDefinition(key = "param",
                                                          description = "test value param",
                                                          type = StringType::class.java)
        override val name = "test3"
        override val description = "Test command"
        override val parameterDefinitions = Array<ParameterDefinition>(arrayOf(valueParam))

        override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {}
    }

    class ImplicitValueParameterTestCommand : ConsoleCommand {

        private val valueParam = ValueParameterDefinition(key = "value",
                                                          description = "test value implicit param",
                                                          type = StringType::class.java)
        override val name = "test4"
        override val description = "Test command"
        override val parameterDefinitions = Array<ParameterDefinition>(arrayOf(valueParam))

        override fun execute(console: AbstractConsole<*>, commands: List<ConsoleCommand>, parameters: Parameters) {}
    }


}