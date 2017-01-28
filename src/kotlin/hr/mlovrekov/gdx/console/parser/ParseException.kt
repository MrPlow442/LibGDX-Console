package hr.mlovrekov.gdx.console.parser

class ParseException(val errorOffset: Int, override val message: String) : RuntimeException(message)