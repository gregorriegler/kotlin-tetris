import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TetrisTest {

    @Test
    fun `starts empty`() {
        val tetris = Tetris(3)

        assertEquals(
            """
            _
            _
            _
            """.trimIndent(),
            tetris.display()
        )
    }

    class Tetris(height: Int) {
        private val field = (0 until height).map { "_" }.toList()

        fun display(): String {
            return field.joinToString(separator = "\n") { it }
        }
    }
}