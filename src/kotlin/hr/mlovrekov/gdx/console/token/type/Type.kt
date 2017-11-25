package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.InspectableInput
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.parser.TraversableInput

interface Type<out T> {
    fun canParse(input: InspectableInput): Boolean
    fun parse(input: TraversableInput, parser: TokenConsoleParser): T
}