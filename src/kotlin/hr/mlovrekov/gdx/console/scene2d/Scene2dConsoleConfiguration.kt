package hr.mlovrekov.gdx.console.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

open class Scene2dConsoleConfiguration {
    companion object {
        private val DEFAULT_LOG_COLOR = Color.WHITE
        private val DEFAULT_DEBUG_COLOR = Color.GOLDENROD
        private val DEFAULT_ERROR_COLOR = Color.RED

        private val DEFAULT_RENDER_BUFFER_SIZE = 250
        private val MIN_RENDER_BUFFER_SIZE = 20
        private val MAX_RENDER_BUFFER_SIZE = 500

        val DEFAULT_CONFIGURATION = Scene2dConsoleConfiguration()
    }

    var logColor = DEFAULT_LOG_COLOR
        private set

    var debugColor = DEFAULT_DEBUG_COLOR
        private set

    var errorColor = DEFAULT_ERROR_COLOR
        private set

    var renderBufferSize = DEFAULT_RENDER_BUFFER_SIZE
        private set(value) {
            field = MathUtils.clamp(value, MIN_RENDER_BUFFER_SIZE, MAX_RENDER_BUFFER_SIZE)
        }

    fun setLogColor(color: Color): Scene2dConsoleConfiguration {
        logColor = color
        return this
    }

    fun setDebugColor(color: Color): Scene2dConsoleConfiguration {
        debugColor = color
        return this
    }

    fun setErrorColor(color: Color): Scene2dConsoleConfiguration {
        errorColor = color
        return this
    }

    fun setRenderBufferSize(renderBufferSize: Int): Scene2dConsoleConfiguration {
        this.renderBufferSize = renderBufferSize
        return this
    }
}