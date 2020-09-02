package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import com.gregorriegler.tetris.view.Color
import com.gregorriegler.tetris.view.Rectangle
import com.gregorriegler.tetris.view.PositionedFrame
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
    private val gameFrame: PositionedFrame

    init {
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = window.innerWidth
        context.canvas.height = window.innerHeight
        document.body!!.appendChild(canvas)
        gameFrame = PositionedFrame.max(tetris, SimpleFrame(window.innerWidth, window.innerHeight))
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
        context.fillRect(
            rectangle.left.toDouble(),
            rectangle.top.toDouble(),
            rectangle.width.toDouble(),
            rectangle.height.toDouble()
        )
        context.strokeStyle = color.enlightenBy(100).asCss()

        val bevelSize = 1
        context.beginPath()
        context.moveTo(rectangle.left.toDouble() + bevelSize,rectangle.top.toDouble() + bevelSize)
        context.lineTo(rectangle.right.toDouble() - bevelSize, rectangle.top.toDouble() + bevelSize)
        context.lineTo(rectangle.right.toDouble() - bevelSize, rectangle.bottom.toDouble() - bevelSize)
        context.stroke()

        context.strokeStyle = color.darkenBy(100).asCss()
        context.beginPath()
        context.moveTo(rectangle.left.toDouble() + bevelSize,rectangle.top.toDouble() + bevelSize)
        context.lineTo(rectangle.left.toDouble() + bevelSize, rectangle.bottom.toDouble() - bevelSize)
        context.lineTo(rectangle.right.toDouble() - bevelSize, rectangle.bottom.toDouble() - bevelSize)
        context.stroke()
    }
}

