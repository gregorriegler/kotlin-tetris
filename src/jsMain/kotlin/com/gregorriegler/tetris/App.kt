package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.Area
import com.gregorriegler.tetris.model.Field
import com.gregorriegler.tetris.model.Filling
import com.gregorriegler.tetris.model.Tetris
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date

fun main() {
    val tetrisJs: TetrisJs = TetrisJs()
}

class TetrisJs {
    private val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
    private val context: CanvasRenderingContext2D
    private val tetris: Tetris = Tetris()

    init {
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
        document.body!!.appendChild(canvas)

        window.setInterval({
            tetris.time(Date.now().toLong())
            draw(tetris.gameDisplay())
        }, 20)
        window.addEventListener("keydown", {
            when ((it as KeyboardEvent).key) {
                "ArrowLeft" -> tetris.left()
                "ArrowRight" -> tetris.right()
                "ArrowDown" -> tetris.speed()
                "ArrowUp" -> tetris.rotate()
                else -> Unit
            }
        })
        window.addEventListener("keyup", {
            when ((it as KeyboardEvent).key) {
                "ArrowDown" -> tetris.normal()
                else -> Unit
            }
        })
    }

    private fun draw(area: Area) {
        context.clearRect(0.0, 0.0, context.canvas.width.toDouble(), context.canvas.height.toDouble())
        area.fields.forEach { field ->
            val frame = Frame(20, 20, 400, 820)
            when (field.filling) {
                Filling.EMPTY -> Unit
                else -> drawFilled(
                    Rectangle.fromArea(frame, area, field),
                    color(field.filling)
                )
            }
        }
    }

    private fun drawFilled(rectangle: Rectangle, color: String) {
        context.fillStyle = color
        context.fillRect(rectangle.left, rectangle.top, rectangle.width, rectangle.height)
    }

    private fun color(filling: Filling): String {
        return when (filling) {
            Filling.BOMB -> "#000000"
            Filling.FILLED -> "#347006"
            Filling.SOIL -> "#ab8a1f"
            else -> "#cccccc"
        }
    }

    private fun width() = canvas.width.toDouble()
    private fun height() = canvas.height.toDouble()
}

class Rectangle(
    val left: Double,
    val top: Double,
    val width: Double,
    val height: Double
) {
    companion object {
        fun fromArea(frame: Frame, area: Area, field: Field): Rectangle {
            val stoneWidth = (frame.width() / area.width()) - 10
            val stoneHeight = (frame.height() / area.height()) - 10
            val squareLeft = frame.left + field.x * stoneWidth + 4
            val squareTop = frame.top + field.y * stoneHeight + 4

            return Rectangle(
                squareLeft.toDouble(),
                squareTop.toDouble(),
                stoneWidth.toDouble(),
                stoneHeight.toDouble()
            )
        }
    }
}

class Frame(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    fun width(): Int = right - left
    fun height(): Int = bottom - top
}