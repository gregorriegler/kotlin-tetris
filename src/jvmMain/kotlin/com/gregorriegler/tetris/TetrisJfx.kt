package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.Tetris
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import java.util.function.Consumer


class TetrisJfx : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(TetrisJfx::class.java)
        }
    }

    private val tetris: Tetris = Tetris()
    private val game: Text = Text(10.0, 50.0, "")
    private val score: Text = Text(50.0, 50.0, "")
    private var gameLoop: GameLoop = GameLoop(tetris, { game.text = it }, { score.text = it })

    override fun start(stage: Stage) {
        val layout = HBox().apply {
            spacing = 20.0
            padding = Insets(20.0, 20.0, 20.0, 20.0)
            children.add(game)
            children.add(score)
            score.font = Font("FreeMono", 20.0)
            game.font = Font("FreeMono", 20.0)
            game.isFocusTraversable = true
            game.requestFocus()
            game.setOnKeyPressed { event ->
                when (event.code) {
                    KeyCode.LEFT -> {
                        tetris.left()
                    }
                    KeyCode.RIGHT -> {
                        tetris.right()
                    }
                    KeyCode.UP -> {
                        tetris.rotate()
                    }
                    KeyCode.DOWN -> {
                        tetris.speed()
                    }
                    else -> {
                    }
                }
            }
            game.setOnKeyReleased { event ->
                when (event.code) {
                    KeyCode.DOWN -> {
                        tetris.normal()
                    }
                    else -> {
                    }
                }
            }
        }
        stage.run {
            scene = Scene(layout)
            show()
            stage.isResizable = false
            scene.window.width = 320.0
            scene.window.height = 600.0
        }

        gameLoop.start()
    }
}

class GameLoop(
    private val tetris: Tetris,
    private val displayGame: Consumer<String>,
    private val displayScore: Consumer<String>
) : AnimationTimer() {

    override fun handle(arg0: Long) {
        tetris.time(System.currentTimeMillis())
        displayGame.accept(tetris.gameDisplayString())
        displayScore.accept("\nScore: " + tetris.score + "\n\nNext Stone:\n" + tetris.nextStone.toString() + "\n" + tetris.gameOver)
    }
}
