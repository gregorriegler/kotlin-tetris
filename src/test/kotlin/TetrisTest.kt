import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TetrisTest {

    private val tetris = Tetris(3)

    @Test
    fun `starts empty`() {
        assertDisplays(
            """
            _
            _
            _
            """,
            0
        )
    }

    @Test
    fun `dropped stone starts at top`() {
        tetris.tick()

        assertDisplays(
            """
            #
            _
            _
            """,
            0
        )
    }

    @Test
    fun `dropped stone falls`() {
        tetris.tick()
        tetris.tick()

        assertDisplays(
            """
            _
            #
            _
            """,
            0
        )
    }

    @Test
    fun `dropped stone falls to bottom`() {
        tetris.tick()
        tetris.tick()
        tetris.tick()

        assertDisplays(
            """
            _
            _
            #
            """,
            0
        )
    }

    @Test
    fun `dropped stone dissolves from bottom`() {
        tetris.tick()
        tetris.tick()
        tetris.tick()
        tetris.tick()

        assertDisplays(
            """
            _
            _
            _
            """,
            1
        )
    }

    private fun assertDisplays(ouput: String, score: Int) {
        assertEquals(
            ouput.trimIndent(),
            tetris.display()
        )
        assertEquals(
            score,
            tetris.score()
        )
    }

}