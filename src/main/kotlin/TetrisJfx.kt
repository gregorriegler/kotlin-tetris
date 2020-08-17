import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage


fun main(args: Array<String>)
{
    launch(JavaFXExample::class.java)
}

class JavaFXExample : Application()
{
    val tetris:Tetris = Tetris(10, 14)
    val text:Text = Text(10.0, 50.0,"test")

    override fun start(stage: Stage)
    {
        val layout = VBox().apply {
            children.add(text)
            text.setFont(Font("FreeMono", 20.0));
            text.isFocusTraversable = true
            text.requestFocus()
            text.setOnKeyPressed { event ->
                if (event.code == KeyCode.LEFT) {
                    tetris.left()
                } else if (event.code == KeyCode.RIGHT) {
                    tetris.right()
                }
            }
        }
        stage.run {
            scene = Scene(layout)
            show()
        }

        var animator: AnimationTimer = object : AnimationTimer() {
            override fun handle(arg0: Long) {
                // update
                // render
                tetris.tick()
                text.text = tetris.display()
                Thread.sleep(300)
            }
        }

        animator.start()
    }



}