package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser

class LiteralType<T>(private val literal: String, private val value: T) : Type<T> {

    override fun canParse(input: Input) = input.matches(literal)

    override fun parse(input: Input, parser: TokenConsoleParser): T {
        input.increment(literal.length)
        return value
    }

}