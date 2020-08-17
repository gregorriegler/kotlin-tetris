import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import java.util.function.Consumer


fun main(args: Array<String>) {
    launch(JavaFXExample::class.java)
}

class JavaFXExample : Application() {
    val tetris: Tetris = Tetris(10, 14)
    val text: Text = Text(10.0, 50.0, "test")
    var gameLoop: GameLoop = GameLoop(tetris) { text.text = it }

    override fun start(stage: Stage) {

        val layout = VBox().apply {
            children.add(text)
            text.setFont(Font("FreeMono", 20.0));
            text.isFocusTraversable = true
            text.requestFocus()
            text.setOnKeyPressed { event ->
                when (event.code) {
                    KeyCode.LEFT -> {
                        tetris.left()
                    }
                    KeyCode.RIGHT -> {
                        tetris.right()
                    }
                    KeyCode.DOWN -> {
                        gameLoop.fast()
                    }
                    else -> {
                    }
                }
            }

            text.setOnKeyReleased { event ->
                when (event.code) {
                    KeyCode.DOWN -> {
                        gameLoop.normal()
                    }
                    else -> {
                    }
                }
            }
        }
        stage.run {
            scene = Scene(layout)
            show()
        }

        gameLoop.start()
    }
}

class GameLoop(tetris: Tetris, display: Consumer<String>) : AnimationTimer() {
    val tetris = tetris
    val display = display
    val normalTickLength = 250
    val fastTickLength = 50
    private var tickLength = normalTickLength

    var lastTickAt: Long = 0

    override fun handle(arg0: Long) {
        if (shouldTick()) {
            lastTickAt = System.currentTimeMillis()
            tetris.tick()
            display.accept(tetris.display())
        }
    }

    private fun shouldTick() = System.currentTimeMillis() - lastTickAt > tickLength

    fun fast() {
        tickLength = fastTickLength
    }

    fun normal() {
        tickLength = normalTickLength
    }
}

private fun AnimationTimer.fast() {
    TODO("Not yet implemented")
}
