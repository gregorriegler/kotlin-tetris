import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class `A Tetris` {

    var timesTicked: Int = 0
    fun tickTimes(tetris: Tetris, times: Int) {
        (timesTicked until timesTicked + times)
            .map { (it + 1) * 250 }
            .forEach { tetris.time(it.toLong()) }
        timesTicked += times
    }

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
            tickTimes(tetris, 1)

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
            tickTimes(tetris, 2)

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
            tickTimes(tetris, 3)

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
            tickTimes(tetris, 4)

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
            tickTimes(tetris, 5)

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
            tickTimes(tetris, 8)

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
        fun `land stone without scoring`() {
            tickTimes(tetris, 4)

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
            tickTimes(tetris, 5)

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
            tickTimes(tetris, 7)

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
            tickTimes(tetris, 8)

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
            tickTimes(tetris, 9)

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
            tickTimes(tetris, 1)

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
        fun `drop another stone`() {
            tickTimes(tetris, 5)

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
            tickTimes(tetris, 1)
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
            tickTimes(tetris, 1)
            tetris.right()
            tickTimes(tetris, 1)

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
            tickTimes(tetris, 3)

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
            tickTimes(tetris, 1)
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
            tickTimes(tetris, 1)
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
        fun `not move more right than possible`() {
            tickTimes(tetris, 1)
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

        @Test
        fun `dissolve filled line`() {
            tetris.left()
            tickTimes(tetris, 4)

            tetris.right()
            tickTimes(tetris, 4)

            tickTimes(tetris, 4)

            assertDisplays(
                tetris,
                """
                ___
                ___
                ___
                """,
                1
            )
        }

        @Test
        fun `stack borders`() {
            tetris.left()
            tickTimes(tetris, 4)

            tetris.left()
            tickTimes(tetris, 3)

            tetris.left()
            tickTimes(tetris, 2)

            tetris.right()
            tickTimes(tetris, 4)

            tetris.right()
            tickTimes(tetris, 3)

            tetris.right()
            tickTimes(tetris, 2)

            assertDisplays(
                tetris,
                """
                #_#
                #_#
                #_#
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