package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

class LiteralType<out T>(private val literal: String, private val value: T) : Type<T> {

    override fun canParse(input: InspectableInput) = input.matches(literal)

    override fun parse(input: TraversableInput, parser: TokenConsoleParser): T {
        input.increment(literal.length)
        return value
    }

}