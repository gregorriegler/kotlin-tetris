import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TetrisTest {

    private var tetris = Tetris(1, 3)

    @Test
    fun `starts empty`() {
        assertDisplays(
            tetris,
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
            tetris,
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
        repeat(2) { tetris.tick() }

        assertDisplays(
            tetris,
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
        repeat(3) { tetris.tick() }

        assertDisplays(
            tetris,
            """
            _
            _
            #
            """,
            0
        )
    }

    @Test
    fun `dropped stone dissolves from bottom as score increases`() {
        repeat(4) { tetris.tick() }

        assertDisplays(
            tetris,
            """
            _
            _
            _
            """,
            1
        )
    }

    @Test
    fun `drops another stone`() {
        repeat(5) { tetris.tick() }

        assertDisplays(
            tetris,
            """
            #
            _
            _
            """,
            1
        )
    }

    @Test
    fun `scores another stone`() {
        repeat(8) { tetris.tick() }

        assertDisplays(
            tetris,
            """
            _
            _
            _
            """,
            2
        )
    }

    private fun assertDisplays(tetris: Tetris, ouput: String, score: Int) {
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