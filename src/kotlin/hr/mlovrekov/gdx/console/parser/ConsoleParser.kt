package hr.mlovrekov.gdx.console.parser

interface ConsoleParser {
    fun parse(line: String): ParseResult
}