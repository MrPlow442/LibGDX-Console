package hr.mlovrekov.gdx.console.token.type

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser

interface Type<T> {
    fun canParse(input: Input): Boolean
    fun parse(input: Input, parser: TokenConsoleParser): T
}