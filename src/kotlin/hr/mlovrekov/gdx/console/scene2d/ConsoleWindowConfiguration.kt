package hr.mlovrekov.gdx.console.scene2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils

class ConsoleWindowConfiguration {
    companion object {
        private val DEFAULT_WIDTH = 500f
        private val MIN_WIDTH = 100f
        private val DEFAULT_HEIGHT = 250f
        private val MIN_HEIGHT = 100f
        private val DEFAULT_TOGGLE_KEY_CODE = Input.Keys.GRAVE
        private val DEFAULT_PREVIOUS_HISTORY_KEY_CODE = Input.Keys.UP
        private val DEFAULT_NEXT_HISTORY_KEY_CODE = Input.Keys.DOWN
        private val DEFAULT_SUBMIT_INPUT_KEY_CODE = Input.Keys.ENTER
    }

    var submitInputKeyCode = DEFAULT_SUBMIT_INPUT_KEY_CODE
        private set(value) {
            when (value) {
                toggleKeyCode, previousHistoryKeyCode, nextHistoryKeyCode -> field = DEFAULT_SUBMIT_INPUT_KEY_CODE
                else                                                      -> field = value
            }
        }

    var toggleKeyCode = DEFAULT_TOGGLE_KEY_CODE
        private set(value) {
            when (value) {
                previousHistoryKeyCode, nextHistoryKeyCode, submitInputKeyCode -> field = DEFAULT_TOGGLE_KEY_CODE
                else                                                           -> field = value
            }
        }

    var previousHistoryKeyCode = DEFAULT_PREVIOUS_HISTORY_KEY_CODE
        private set(value) {
            when (value) {
                toggleKeyCode, nextHistoryKeyCode, submitInputKeyCode -> field = DEFAULT_PREVIOUS_HISTORY_KEY_CODE
                else                                                  -> field = value
            }
        }

    var nextHistoryKeyCode = DEFAULT_NEXT_HISTORY_KEY_CODE
        private set(value) {
            when (value) {
                toggleKeyCode, previousHistoryKeyCode, submitInputKeyCode -> field = DEFAULT_NEXT_HISTORY_KEY_CODE
                else                                                      -> field = value
            }
        }

    var width = DEFAULT_WIDTH
        private set(value) {
            field = MathUtils.clamp(value, MIN_WIDTH, Gdx.graphics.width.toFloat())
        }

    var height = DEFAULT_HEIGHT
        private set(value) {
            field = MathUtils.clamp(value, MIN_HEIGHT, Gdx.graphics.height.toFloat())
        }

    fun setToggleKeyCode(keyCode: Int): ConsoleWindowConfiguration {
        this.toggleKeyCode = keyCode
        return this
    }

    fun setPreviousHistoryKeyCode(keyCode: Int): ConsoleWindowConfiguration {
        this.previousHistoryKeyCode = keyCode
        return this
    }

    fun setNextHistoryKeyCode(keyCode: Int): ConsoleWindowConfiguration {
        this.nextHistoryKeyCode = keyCode
        return this
    }

    fun setWidth(width: Float): ConsoleWindowConfiguration {
        this.width = width
        return this
    }

    fun setHeight(height: Float): ConsoleWindowConfiguration {
        this.height = height
        return this
    }
}