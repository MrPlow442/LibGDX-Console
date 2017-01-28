package hr.mlovrekov.gdx.console.token

import hr.mlovrekov.gdx.console.parser.Input
import hr.mlovrekov.gdx.console.parser.TokenBasedConsoleParser

interface TypeParser<out T> {
    fun canParse(input: Input): Boolean
    fun parse(input: Input, parser: TokenBasedConsoleParser): T
}