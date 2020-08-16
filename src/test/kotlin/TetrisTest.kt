import org.junit.jupiter.api.Assertions.assertEquals
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
        fun `land stone at the bottom`() {
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
        fun `land stone at the bottom`() {
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
        fun `land stone without scoring`() {
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

        @Test
        fun `drop another stone`() {
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

        @Test
        fun `stack landed stones`() {
            repeat(7) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                __
                #_
                #_
                """,
                0
            )
        }

        @Test
        fun `stack all the way to the top`() {
            repeat(8) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                #_
                #_
                #_
                """,
                0
            )
        }

        @Test
        fun `report game over`() {
            repeat(9) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                ##
                Game Over
                ##
                """,
                0
            )
        }
    }

    @Nested
    inner class `with three columns should` {
        private var tetris = Tetris(3, 3)

        @Test
        fun `start empty`() {
            assertDisplays(
                tetris,
                """
                ___
                ___
                ___
                """,
                0
            )
        }

        @Test
        fun `drop stone in the center`() {
            tetris.tick()

            assertDisplays(
                tetris,
                """
                _#_
                ___
                ___
                """,
                0
            )
        }

        @Test
        fun `continue dropping a stone in the center`() {
            repeat(2) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                ___
                _#_
                ___
                """,
                0
            )
        }

        @Test
        fun `land stone at the bottom`() {
            repeat(3) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                ___
                ___
                _#_
                """,
                0
            )
        }

        @Test
        fun `land stone without scoring`() {
            repeat(4) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                ___
                ___
                _#_
                """,
                0
            )
        }

        @Test
        fun `drop another stone`() {
            repeat(5) { tetris.tick() }

            assertDisplays(
                tetris,
                """
                _#_
                ___
                _#_
                """,
                0
            )
        }

        @Test
        fun `move stone to the right`() {
            tetris.tick()
            tetris.right()

            assertDisplays(
                tetris,
                """
                __#
                ___
                ___
                """,
                0
            )
        }

        @Test
        fun `stay right`() {
            tetris.tick()
            tetris.right()
            tetris.tick()

            assertDisplays(
                tetris,
                """
                ___
                __#
                ___
                """,
                0
            )
        }

        @Test
        fun `land right`() {
            tetris.right()
            tetris.tick()
            tetris.tick()
            tetris.tick()

            assertDisplays(
                tetris,
                """
                ___
                ___
                __#
                """,
                0
            )
        }

        @Test
        fun `move stone to the left`() {
            tetris.tick()
            tetris.left()

            assertDisplays(
                tetris,
                """
                #__
                ___
                ___
                """,
                0
            )
        }

        @Test
        fun `not move more left than 0`() {
            tetris.tick()
            tetris.left()
            tetris.left()

            assertDisplays(
                tetris,
                """
                #__
                ___
                ___
                """,
                0
            )
        }

        @Test
        fun `not move more right than width`() {
            tetris.tick()
            tetris.right()
            tetris.right()

            assertDisplays(
                tetris,
                """
                __#
                ___
                ___
                """,
                0
            )
        }
    }

    private fun assertDisplays(tetris: Tetris, output: String, score: Int) {
        assertEquals(
            "\n" + output.trimIndent() + "\n",
            "\n" + tetris.display() + "\n"
        )
        assertEquals(
            score,
            tetris.score()
        )
    }

}