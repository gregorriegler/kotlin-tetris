package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import com.gregorriegler.tetris.view.Color
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date

fun main() {
    val tetrisJs = TetrisJs()
    tetrisJs.start()
}

class TetrisJs {
    private val displayCanvas: HTMLCanvasElement = document.getElementById("display") as HTMLCanvasElement
    private val displayCanvasContext: CanvasRenderingContext2D
    private val nextStoneCanvas: HTMLCanvasElement = document.getElementById("next-stone") as HTMLCanvasElement
    private val nextStoneCanvasContext: CanvasRenderingContext2D
    private val score: HTMLParagraphElement = document.getElementById("score") as HTMLParagraphElement
    private val tetris: Tetris = Tetris()
    private val display: Frame = SimpleFrame.max(tetris, SimpleFrame(window.innerWidth, window.innerHeight))

    init {
        displayCanvasContext = displayCanvas.getContext("2d") as CanvasRenderingContext2D
        nextStoneCanvasContext = nextStoneCanvas.getContext("2d") as CanvasRenderingContext2D
    }

    fun start() {
        window.setInterval({
            tetris.time(Date.now().toLong())
            drawGame(tetris.gameDisplay())
            drawNextStone(tetris.nextStone)
            score.textContent = tetris.score.toString()
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

    private fun drawGame(debris: Debris) {
        displayCanvasContext.canvas.width = display.width
        displayCanvasContext.canvas.height = display.height
        clearCanvas(displayCanvasContext)
        debris.asStones(display).forEach { drawFilled(it, displayCanvasContext) }
    }

    private fun drawNextStone(nextStone: Structure) {
        nextStoneCanvasContext.canvas.width = 100
        nextStoneCanvasContext.canvas.height = 100
        clearCanvas(nextStoneCanvasContext)
        nextStone.asStones(SimpleFrame(100, 100), 0, listOf(Color.grey)).forEach { drawFilled(it, nextStoneCanvasContext) }
    }

    private fun clearCanvas(canvasRenderingContext2D: CanvasRenderingContext2D) {
        canvasRenderingContext2D.clearRect(0.0, 0.0, canvasRenderingContext2D.canvas.width.toDouble(), canvasRenderingContext2D.canvas.height.toDouble())
    }

    private fun drawFilled(stone: TetrisStone, canvasContext: CanvasRenderingContext2D) {
        canvasContext.fillStyle = stone.color.asCss()
        canvasContext.fillRect(
            stone.x.toDouble(),
            stone.y.toDouble(),
            stone.width.toDouble(),
            stone.height.toDouble()
        )
        canvasContext.strokeStyle = stone.color.enlightenBy(100).asCss()

        val bevelSize = 1
        canvasContext.beginPath()
        canvasContext.moveTo(stone.x.toDouble() + bevelSize, stone.y.toDouble() + bevelSize)
        canvasContext.lineTo(stone.rightSide.toDouble() - bevelSize, stone.y.toDouble() + bevelSize)
        canvasContext.lineTo(stone.rightSide.toDouble() - bevelSize, stone.bottom.toDouble() - bevelSize)
        canvasContext.stroke()

        canvasContext.strokeStyle = stone.color.darkenBy(100).asCss()
        canvasContext.beginPath()
        canvasContext.moveTo(stone.x.toDouble() + bevelSize, stone.y.toDouble() + bevelSize)
        canvasContext.lineTo(stone.x.toDouble() + bevelSize, stone.bottom.toDouble() - bevelSize)
        canvasContext.lineTo(stone.rightSide.toDouble() - bevelSize, stone.bottom.toDouble() - bevelSize)
        canvasContext.stroke()
    }
}

