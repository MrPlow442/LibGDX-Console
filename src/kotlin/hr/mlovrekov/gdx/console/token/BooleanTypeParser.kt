package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

class BooleanTypeParser(val keyword: BooleanKeyword) : TypeParser<Boolean> {
    enum class BooleanKeyword(val literal: String, val value: Boolean) {
        TRUE("true", true),
        FALSE("false", false);
    }

    override fun canParse(input: Input) = input.matchesLiteral(keyword.literal)

    override fun parse(input: Input, parser: TokenBasedConsoleParser): Boolean {
        input.increment(keyword.literal.length)
        return keyword.value
    }

}