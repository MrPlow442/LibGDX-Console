package hr.mlovrekov.gdx.console.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.FlushablePool
import hr.mlovrekov.gdx.console.AbstractConsole
import hr.mlovrekov.gdx.console.Console
import hr.mlovrekov.gdx.console.command.HelloCommand
import hr.mlovrekov.gdx.console.command.HelpCommand
import hr.mlovrekov.gdx.console.command.ListCommand
import hr.mlovrekov.gdx.console.command.LogCommand
import hr.mlovrekov.gdx.console.history.ArrayConsoleHistory
import hr.mlovrekov.gdx.console.parser.TokenConsoleParser
import hr.mlovrekov.gdx.console.util.ConstrainedQueue
import com.badlogic.gdx.utils.Array as GdxArray

class ConsoleWindow(skin: Skin)
    : Window("Console", skin) {

    private val toggleKeyCode: Int = Input.Keys.GRAVE
    private val previousHistoryKeyCode: Int = Input.Keys.UP
    private val nextHistoryKeyCode: Int = Input.Keys.DOWN
    private val completionKeyCode: Int = Input.Keys.TAB

    private val consoleLogTable = Table(skin)
    private val consoleLogScrollPane = ScrollPane(consoleLogTable, skin)
    private val consoleTextField = TextField("", skin)

    private val console: Console = Scene2dConsole()

    private val textFieldListener = { textField: TextField, key: Char ->
        if (key == '\n' || key == '\r') {
            val line = textField.text
            textField.text = ""
            console.execute(line)
        }
    }

    private val inputListener = object : InputListener() {
        override fun keyDown(event: InputEvent, keycode: Int) = when (keycode) {
            previousHistoryKeyCode -> {
                consoleTextField.text = console.history.previous()
                true
            }
            nextHistoryKeyCode     -> {
                consoleTextField.text = console.history.next()
                true
            }
            completionKeyCode      -> {
                val completions = console.getAvailableCompletions(consoleTextField.text)
                if (completions.size == 1) {
                    consoleTextField.text = completions.first()
                } else if (completions.size > 1) {
                    console.print(completions.joinToString(" "))
                }
                true
            }
            else                   -> false
        }
    }

    private val toggleConsoleInputListener = object : InputListener() {
        override fun keyDown(event: InputEvent, keycode: Int) = when (keycode) {
            toggleKeyCode -> {
                isVisible = !isVisible
                consoleTextField.isDisabled = !isVisible
//                event.handle()
//                if (isVisible) {
//                    stage.keyboardFocus = consoleTextField
//                }
                true
            }
            else          -> false
        }
    }

    init {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height * 0.5f

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
        add(consoleLogScrollPane).expand().fill().padLeft(5f)
        row()
        add(consoleTextField).expandX().fillX()
    }

    override fun setStage(stage: Stage?) {
        this.stage?.removeListener(toggleConsoleInputListener)
        stage?.addListener(toggleConsoleInputListener)
        super.setStage(stage)
    }

    inner class Scene2dConsole : AbstractConsole<Label>() {
        override val history = ArrayConsoleHistory(20)
        override val parser = TokenConsoleParser(commands)

        private val labelPool = object : FlushablePool<Label>() {
            override fun newObject() = Label("", skin)

            override fun reset(label: Label) {
                label.setText("")
                label.color = skin.get<Label.LabelStyle>(Label.LabelStyle::class.java).fontColor
            }
        }

        init {
            registerCommand(ListCommand())
            registerCommand(LogCommand())
            registerCommand(HelpCommand())
            registerCommand(HelloCommand())
        }

        override fun buildLine(message: String, type: Console.LogType): Label = labelPool
                .obtain()
                .apply {
                    setText(message)
                    color = getLogColor(type)
                }

        override fun onPrint(lines: ConstrainedQueue<Label>, evictedLine: Label?) {
            if (evictedLine != null) {
                labelPool.free(evictedLine)
            }

            consoleLogTable.clear()
            consoleLogTable.add()
                    .expand()
                    .fill()
                    .row()
            lines.forEach {
                consoleLogTable.add(it)
                        .expandX()
                        .fillX()
                        .bottom()
                        .left()
                        .row()
            }
            consoleLogScrollPane.validate()
            consoleLogScrollPane.scrollPercentY = 100f
        }

        private fun getLogColor(type: Console.LogType): Color = when (type) {
            Console.LogType.INFO  -> Color.WHITE
            Console.LogType.ERROR -> Color.RED
            Console.LogType.DEBUG -> Color.CYAN
        }
    }
}

