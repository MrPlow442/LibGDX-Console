package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class CommandNameParser : TypeParser<String> {

    override fun canParse(input: Input): Boolean {
        return input.isAtLetter() || input.isAtChar('/')
    }

    override fun parse(input: Input, parser: TokenBasedConsoleParser): String {
        val output = StringBuilder()
        while (!input.isEol()) {
            if (input.isAtWhitespace()) {
                input.increment()
                break
            }
            output.append(input.getAndIncrement())
        }
        return output.toString()
    }

}