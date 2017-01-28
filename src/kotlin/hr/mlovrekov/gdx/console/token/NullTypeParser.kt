package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class NullTypeParser : TypeParser<Any?> {
    companion object {
        const val NULL_KEYWORD = "null"
    }

    override fun canParse(input: Input) = input.matchesLiteral(NULL_KEYWORD)

    override fun parse(input: Input, parser: TokenBasedConsoleParser): Any? {
        input.increment(NULL_KEYWORD.length)
        return null
    }

}