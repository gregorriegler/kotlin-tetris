import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class `A Tetris` {

    @Nested
    inner class `with a single column should` {
        private var tetris = Tetris(1, 3)

        @Test
        fun `start empty`() {
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
        fun `drop a stone from the top`() {
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
        fun `continue dropping a stone`() {
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
        fun `land stone on the bottom`() {
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
        fun `dissolve stone from bottom as score increases`() {
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
        fun `drop another stone`() {
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
        fun `score another stone`() {
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
    }

    @Nested
    inner class `with two columns should` {
        private var tetris = Tetris(2, 3)

        @Test
        fun `start empty`() {
            assertDisplays(
                tetris,
                """
                __
                __
                __
                """,
                0
            )
        }

        @Test
        fun `drop a stone from the top`() {
            tetris.tick()

            assertDisplays(
                tetris,
                """
                #_
                __
                __
                """,
                0
            )
        }

        @Test
        fun `continue dropping a stone`() {
            repeat(2) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                __
                #_
                __
                """,
                0
            )
        }

        @Test
        fun `land stone on the bottom`() {
            repeat(3) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                __
                __
                #_
                """,
                0
            )
        }

        @Test
        fun `stone persists without scoring`() {
            repeat(4) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                __
                __
                #_
                """,
                0
            )
        }

        @Disabled // for this the position lifecycle belongs to the stone
        @Test
        fun `another stone falls`() {
            repeat(5) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                #_
                __
                #_
                """,
                0
            )
        }
    }

    private fun assertDisplays(tetris: Tetris, output: String, score: Int) {
        assertEquals(
            output.trimIndent(),
            tetris.display()
        )
        assertEquals(
            score,
            tetris.score()
        )
    }

}