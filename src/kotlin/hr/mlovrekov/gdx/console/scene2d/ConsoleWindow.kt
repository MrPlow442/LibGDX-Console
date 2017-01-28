package hr.mlovrekov.gdx.console.scene2d

import com.badlogic.gdx.ApplicationLogger
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import hr.mlovrekov.gdx.console.buffer.ConsoleBufferObserver
import com.badlogic.gdx.utils.Array as GdxArray

class ConsoleWindow(skin: Skin, configuration: ConsoleWindowConfiguration)
    : Window("Console", skin), ApplicationLogger {

    private val toggleKeyCode: Int = configuration.toggleKeyCode
    private val previousHistoryKeyCode: Int = configuration.previousHistoryKeyCode
    private val nextHistoryKeyCode: Int = configuration.nextHistoryKeyCode

    private val consoleLogTable = Table(skin)
    private val consoleLogScrollPane = ScrollPane(consoleLogTable, skin)
    private val consoleTextField = TextField("", skin)

    private val console = Scene2dConsole(skin, Scene2dConsoleConfiguration.DEFAULT_CONFIGURATION)

    private val textFieldListener = { textField: TextField, key: Char ->
        if (System.lineSeparator().contains(key)) {
            val line = textField.text
            textField.text = ""
            console.execute(line)
        }
    }

    private val inputListener = object : InputListener() {
        override fun keyDown(event: InputEvent, keycode: Int): Boolean {
            return when (keycode) {
                previousHistoryKeyCode -> {
                    consoleTextField.text = console.previousHistory()
                    true
                }
                nextHistoryKeyCode     -> {
                    consoleTextField.text = console.nextHistory()
                    true
                }
                else                   -> false
            }
        }
    }

    private val consoleBufferObserver = ConsoleBufferObserver { buffer ->
        consoleLogTable.clear()
        consoleLogTable.add().expand().fill().row()
        console.labels.forEach {
            consoleLogTable.add(it).expandX().fillX().bottom().left().row()
        }
        consoleLogScrollPane.validate()
        consoleLogScrollPane.scrollPercentY = 100f
    }

    private val toggleConsoleInputListener = object : InputListener() {
        override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
            return when (keycode) {
                toggleKeyCode -> {
                    isVisible = !isVisible
                    consoleTextField.isDisabled = !isVisible
                    if (isVisible) {
                        stage.keyboardFocus = consoleTextField
                    }
                    true
                }
                else          -> false
            }
        }
    }

    init {
        width = configuration.width
        height = configuration.height

        padTop(20f)
        isMovable = true
        isModal = true
        isResizable = true
        isVisible = false
        setKeepWithinStage(true)

        consoleLogScrollPane.apply {
            setFadeScrollBars(false)
            setScrollbarsOnTop(false)
            setOverscroll(false, false)
        }

        consoleTextField.apply {
            setFocusTraversal(false)
            setTextFieldListener(textFieldListener)
            addListener(inputListener)
            isDisabled = true
        }

        defaults()
        add(consoleLogScrollPane).expand().fill()
        row()
        add(consoleTextField).expandX().fillX()

        console.addBufferObserver(consoleBufferObserver)
    }

    override fun setStage(stage: Stage?) {
        this.stage?.removeListener(toggleConsoleInputListener)
        stage?.addListener(toggleConsoleInputListener)
        super.setStage(stage)
    }

    override fun debug(tag: String, message: String) {
        console.debug(tag, message)
    }

    override fun debug(tag: String, message: String, exception: Throwable) {
        console.debug(tag, message, exception)
    }

    override fun error(tag: String, message: String) {
        console.error(tag, message)
    }

    override fun error(tag: String, message: String, exception: Throwable) {
        console.error(tag, message, exception)
    }

    override fun log(tag: String, message: String) {
        console.log(tag, message)
    }

    override fun log(tag: String, message: String, exception: Throwable) {
        console.log(tag, message, exception)
    }

}

