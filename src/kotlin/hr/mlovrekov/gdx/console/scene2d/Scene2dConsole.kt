package hr.mlovrekov.gdx.console.scene2d

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.ConsoleConfiguration
import hr.mlovrekov.gdx.console.buffer.ArrayConsoleBuffer
import hr.mlovrekov.gdx.console.buffer.ConsoleBufferObserver
import com.badlogic.gdx.utils.Array as GdxArray

class Scene2dConsole(val skin: Skin, scene2dConfiguration: Scene2dConsoleConfiguration) :
        AbstractConsole(ConsoleConfiguration.DEFAULT_CONSOLE_CONFIGURATION), ApplicationLogger {

    private val colorMarkupEnabled = skin.getFont("default").data.markupEnabled

    private val logColorMarkup = if (colorMarkupEnabled) "[#${scene2dConfiguration.logColor}]" else ""
    private val debugColorMarkup = if (colorMarkupEnabled) "[#${scene2dConfiguration.debugColor}]" else ""
    private val errorColorMarkup = if (colorMarkupEnabled) "[#${scene2dConfiguration.errorColor}]" else ""
    private val endTagMarkup = if (colorMarkupEnabled) "[]" else ""

    val labels = GdxArray<Label>(scene2dConfiguration.renderBufferSize)

    init {
        buffer.addObserver(ConsoleBufferObserver { consoleBuffer ->
            if (consoleBuffer !is ArrayConsoleBuffer) {
                return@ConsoleBufferObserver
            }
            resetLabels()
            setLabels(consoleBuffer.buffer)
        })
    }

    override fun log(tag: String, message: String) {
        printLine("$logColorMarkup$tag:$message$endTagMarkup")
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        printLine("$logColorMarkup$tag:$message ${exception.message}$endTagMarkup")
    }

    override fun error(tag: String, message: String) {
        printLine("$errorColorMarkup$tag:$message$endTagMarkup")
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        printLine("$errorColorMarkup$tag:$message ${exception.message}$endTagMarkup")
    }

    override fun debug(tag: String, message: String) {
        printLine("$debugColorMarkup$tag:$message$endTagMarkup")
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        printLine("$debugColorMarkup$tag:$message ${exception.message}$endTagMarkup")
    }

    private fun resetLabels() {
        labels.forEach {
            resetLabel(it)
        }
    }

    private fun setLabels(lines: GdxArray<String>) {
        lines.forEachIndexed { i, line ->
            if (i < labels.size) {
                labels[i].setText(line)
            } else {
                labels.add(Label(line, skin).apply { setWrap(true) })
            }
        }
    }

    private fun resetLabel(label: Label) {
        label.setText("")
    }
}