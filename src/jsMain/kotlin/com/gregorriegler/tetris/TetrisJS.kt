package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import com.gregorriegler.tetris.view.Color
import com.gregorriegler.tetris.view.Palette
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date

fun main() {
    val tetrisJs = TetrisJs()
    tetrisJs.start()
}

class TetrisJs {
    private val displayCanvas: HTMLCanvasElement = document.getElementById("display") as HTMLCanvasElement
    private val displayCanvasContext: CanvasRenderingContext2D = displayCanvas.getContext("2d") as CanvasRenderingContext2D
    private val nextStoneCanvas: HTMLCanvasElement = document.getElementById("next-stone") as HTMLCanvasElement
    private val nextStoneCanvasContext: CanvasRenderingContext2D = nextStoneCanvas.getContext("2d") as CanvasRenderingContext2D
    private val score: HTMLParagraphElement = document.getElementById("score") as HTMLParagraphElement
    private val depth: HTMLParagraphElement = document.getElementById("depth") as HTMLParagraphElement
    private val game: HTMLDivElement = document.getElementById("game") as HTMLDivElement
    private val fullscreen: HTMLButtonElement = document.getElementById("fullscreen") as HTMLButtonElement
    private val gameOver: HTMLDivElement = document.getElementById("game-over") as HTMLDivElement
    private val playAgain: HTMLButtonElement = document.getElementById("play-again") as HTMLButtonElement
    private var tetris: Tetris = Tetris()

    fun start() {
        window.setInterval({
            tetris.time(Date.now().toLong())
            drawGame(tetris.gameDisplay())
            drawNextStone(tetris.nextStone)
            score.textContent = tetris.score.toString()
            depth.textContent = tetris.depth.toString()
            drawGameOver()
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

        var startX = 0
        var startY = 0
        window.addEventListener("touchstart", {
            it as TouchEvent
            startX = it.changedTouches[0]?.clientX!!
            startY = it.changedTouches[0]?.clientY!!
        })
        window.addEventListener("touchmove", {
            it as TouchEvent
            val moveX = it.changedTouches[0]?.clientX!!
            val moveY = it.changedTouches[0]?.clientY!!

            if(moveX <= startX - 5){
                tetris.left()
            } else if(moveX >= startX + 5){
                tetris.right()
            }
            if(moveY >= startY + 10) {
                tetris.speed()
            } else {
                tetris.normal()
            }
            startX = it.changedTouches[0]?.clientX!!
            startY = it.changedTouches[0]?.clientY!!
        })
        window.addEventListener("touchend", {
            tetris.normal()
        })

        window.addEventListener("click", {
            tetris.rotate()
        })

        playAgain.addEventListener("click", {
            restart()
        })

        fullscreen.addEventListener("click", {toggleFullscreen()})
    }

    private fun toggleFullscreen() {
        if(document.fullscreenElement == null) {
            document.body?.requestFullscreen()
        } else {
            document.exitFullscreen()
        }
    }

    private fun restart() {
        tetris = Tetris()
    }

    private fun drawGameOver() {
        if(tetris.gameOver.isNotBlank()) {
            this.gameOver.style.display = "block"
        } else {
            this.gameOver.style.display = "none"
        }
    }

    private fun drawGame(debris: Debris) {
        val display: Frame = SimpleFrame.max(tetris, SimpleFrame(displayCanvas.clientWidth, displayCanvas.clientHeight))
        displayCanvasContext.canvas.width = display.width
        displayCanvasContext.canvas.height = display.height
        clearCanvas(displayCanvasContext)

        drawBackgroundGradient(display)
        debris.asStones(display).forEach { drawFilled(it, displayCanvasContext) }
    }

    private fun drawBackgroundGradient(display: Frame) {
        val maxHeight = 5000.0
        val stoneSize = 30
        val gradientTop = 0.0 - tetris.depth * stoneSize
        val gradientBottom = maxHeight - tetris.depth * stoneSize
        val gradient = displayCanvasContext.createLinearGradient(0.0, gradientTop, 0.0, gradientBottom)
        gradient.addColorStop(0.0, "#ccffff")
        gradient.addColorStop(0.04, "#ccffff")
        gradient.addColorStop(0.12, "aliceblue")
        gradient.addColorStop(0.16, "white")
        gradient.addColorStop(0.2, "#CBCBCB")
        gradient.addColorStop(1.0, "#9a9b9b")
        displayCanvasContext.fillStyle = gradient
        displayCanvasContext.fillRect(0.0, 0.0, display.width.toDouble(), display.height.toDouble())
    }

    private fun drawNextStone(nextStone: Structure) {
        nextStoneCanvasContext.canvas.width = 100
        nextStoneCanvasContext.canvas.height = 100
        clearCanvas(nextStoneCanvasContext)
        nextStone.asStones(SimpleFrame(100, 100), 0, Palette(Color.grey)).forEach { drawFilled(it, nextStoneCanvasContext) }
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

        val bevelSize = 4
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