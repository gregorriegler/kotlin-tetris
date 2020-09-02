package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import com.gregorriegler.tetris.view.Color
import com.gregorriegler.tetris.model.SimplePositionedFrame
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
        gameFrame = SimplePositionedFrame.max(tetris, SimpleFrame(window.innerWidth, window.innerHeight))
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
        clearScreen()
        area.fields.forEach { field ->
            when (field.filling) {
                Filling.EMPTY -> Unit
                else -> drawFilled(
                    SimplePositionedFrame.tetrisStone(gameFrame, area, field),
                    Filling.color(field.filling)
                )
            }
        }
    }

    private fun clearScreen() {
        context.clearRect(0.0, 0.0, context.canvas.width.toDouble(), context.canvas.height.toDouble())
    }

    private fun drawFilled(frame: PositionedFrame, color: Color) {
        context.fillStyle = color.asCss()
        context.fillRect(
            frame.x.toDouble(),
            frame.y.toDouble(),
            frame.width.toDouble(),
            frame.height.toDouble()
        )
        context.strokeStyle = color.enlightenBy(100).asCss()

        val bevelSize = 1
        context.beginPath()
        context.moveTo(frame.x.toDouble() + bevelSize,frame.y.toDouble() + bevelSize)
        context.lineTo(frame.rightSide.toDouble() - bevelSize, frame.y.toDouble() + bevelSize)
        context.lineTo(frame.rightSide.toDouble() - bevelSize, frame.bottom.toDouble() - bevelSize)
        context.stroke()

        context.strokeStyle = color.darkenBy(100).asCss()
        context.beginPath()
        context.moveTo(frame.x.toDouble() + bevelSize,frame.y.toDouble() + bevelSize)
        context.lineTo(frame.x.toDouble() + bevelSize, frame.bottom.toDouble() - bevelSize)
        context.lineTo(frame.rightSide.toDouble() - bevelSize, frame.bottom.toDouble() - bevelSize)
        context.stroke()
    }
}

