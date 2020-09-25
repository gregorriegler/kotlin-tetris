package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.js.Date

fun main() {
    val tetrisJs = TetrisJs()
    tetrisJs.start()
}

class TetrisJs {
    private val game: HTMLDivElement = document.createElement("div") as HTMLDivElement
    private val canvas: HTMLCanvasElement = document.createElement("canvas") as HTMLCanvasElement
    private val score: HTMLDivElement = document.createElement("div") as HTMLDivElement
    private val context: CanvasRenderingContext2D
    private val tetris: Tetris = Tetris()
    private val display: Frame = SimpleFrame.max(tetris, SimpleFrame(window.innerWidth, window.innerHeight))

    init {
        game.id = "game"
        score.id = "score"
        document.body!!.appendChild(game)
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.canvas.width = display.width
        context.canvas.height = display.height
        game.appendChild(canvas)
        game.appendChild(score)
    }

    fun start() {
        window.setInterval({
            tetris.time(Date.now().toLong())
            draw(tetris.gameDisplay())
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

    private fun draw(debris: Debris) {
        clearScreen()
        debris.asStones(display).forEach { drawFilled(it) }
    }

    private fun clearScreen() {
        context.clearRect(0.0, 0.0, context.canvas.width.toDouble(), context.canvas.height.toDouble())
    }

    private fun drawFilled(stone: TetrisStone) {
        context.fillStyle = stone.color.asCss()
        context.fillRect(
            stone.x.toDouble(),
            stone.y.toDouble(),
            stone.width.toDouble(),
            stone.height.toDouble()
        )
        context.strokeStyle = stone.color.enlightenBy(100).asCss()

        val bevelSize = 1
        context.beginPath()
        context.moveTo(stone.x.toDouble() + bevelSize, stone.y.toDouble() + bevelSize)
        context.lineTo(stone.rightSide.toDouble() - bevelSize, stone.y.toDouble() + bevelSize)
        context.lineTo(stone.rightSide.toDouble() - bevelSize, stone.bottom.toDouble() - bevelSize)
        context.stroke()

        context.strokeStyle = stone.color.darkenBy(100).asCss()
        context.beginPath()
        context.moveTo(stone.x.toDouble() + bevelSize, stone.y.toDouble() + bevelSize)
        context.lineTo(stone.x.toDouble() + bevelSize, stone.bottom.toDouble() - bevelSize)
        context.lineTo(stone.rightSide.toDouble() - bevelSize, stone.bottom.toDouble() - bevelSize)
        context.stroke()
    }
}

