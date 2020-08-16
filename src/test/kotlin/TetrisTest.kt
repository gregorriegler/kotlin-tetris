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
            """
        )
    }

    @Test
    fun `dropped stone starts at top`() {
        tetris.drop()

        assertDisplays(
            """
            #
            _
            _
            """
        )
    }

    @Test
    fun `dropped stone falls`() {
        tetris.drop()
        tetris.tick()

        assertDisplays(
            """
            _
            #
            _
            """
        )
    }

    @Test
    fun `dropped stone falls to bottom`() {
        tetris.drop()
        tetris.tick()
        tetris.tick()

        assertDisplays(
            """
            _
            _
            #
            """
        )
    }

    @Test
    fun `dropped stone dissolves from bottom`() {
        tetris.drop()
        tetris.tick()
        tetris.tick()
        tetris.tick()

        assertDisplays(
            """
            _
            _
            _
            """
        )
    }

    private fun assertDisplays(s: String) {
        assertEquals(
            s.trimIndent(),
            tetris.display()
        )
    }

}