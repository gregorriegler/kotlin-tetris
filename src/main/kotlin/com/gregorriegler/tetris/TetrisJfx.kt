package com.gregorriegler.tetris

import com.gregorriegler.tetris.model.Frame
import com.gregorriegler.tetris.model.Structure
import com.gregorriegler.tetris.model.Tetris
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import java.util.function.Consumer


fun main() {
    launch(JavaFXExample::class.java)
}

class JavaFXExample : Application() {
    private val tetris: Tetris = Tetris(Frame(10, 14), listOf(
        Structure.create2by2(),
        Structure.create1by4(),
        Structure.create3and1(),
        Structure.createL(),
        Structure.createMirrorL(),
    ))
    private val game: Text = Text(10.0, 50.0, "")
    private val score: Text = Text(50.0, 50.0, "")
    private var gameLoop: GameLoop = GameLoop(tetris, { game.text = it }, {score.text = it})

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
            scene.window.height = 400.0
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
        displayGame.accept(tetris.gameDisplay())
        displayScore.accept("\nScore: " + tetris.score + "\n\nNext Stone:\n" + tetris.nextStone.toString() + "\n" + tetris.gameOver)
    }
}
