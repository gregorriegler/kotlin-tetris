package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import com.gregorriegler.tetris.view.Color
import com.gregorriegler.tetris.view.ViewFrame
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date

fun main() {
    val tetrisJs = TetrisJs()
    tetrisJs.start()
}

class TetrisJs {
    private val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
    private val context: CanvasRenderingContext2D
    private val tetris: Tetris = Tetris()
    private val gameFrame: ViewFrame

    init {
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
        document.body!!.appendChild(canvas)
        gameFrame = ViewFrame.max(tetris, SimpleFrame(window.innerWidth, window.innerHeight))
    }

    fun start() {
        window.setInterval({
            tetris.time(Date.now().toLong())
            draw(tetris.gameDisplay())
        }, 10)
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
            when (field.filling) {
                Filling.EMPTY -> Unit
                else -> drawFilled(
                    Rectangle.fromArea(gameFrame, area, field),
                    Filling.color(field.filling)
                )
            }
        }
    }

    private fun drawFilled(rectangle: Rectangle, color: Color) {
        context.fillStyle = color.asCss()
        context.fillRect(rectangle.left, rectangle.top, rectangle.width, rectangle.height)
        context.strokeStyle = color.enlightenBy(100).asCss()
        context.beginPath()
        val bevelSize = 1
        context.moveTo(rectangle.left + bevelSize,rectangle.top + bevelSize)
        context.lineTo(rectangle.right - bevelSize, rectangle.top + bevelSize)
        context.lineTo(rectangle.right - bevelSize, rectangle.bottom - bevelSize)
        context.stroke()

        context.strokeStyle = color.darkenBy(100).asCss()
        context.beginPath()
        context.moveTo(rectangle.left + bevelSize,rectangle.top + bevelSize)
        context.lineTo(rectangle.left + bevelSize, rectangle.bottom - bevelSize)
        context.lineTo(rectangle.right - bevelSize, rectangle.bottom - bevelSize)
        context.stroke()
    }
}

class Rectangle(
    val left: Double,
    val top: Double,
    val width: Double,
    val height: Double,

) {
    val right: Double = left + width
    val bottom: Double = top + height

    companion object {
        fun fromArea(frame: ViewFrame, area: Area, field: Field): Rectangle {
            val stoneWidth = frame.width / area.width
            val stoneHeight = frame.height / area.height
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

