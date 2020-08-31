package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
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
        private var tetris = Tetris(TetrisFrame(1, 3), listOf(Structure.create1by1()), 0)

        @Test
        fun `start empty`() {
            assertDisplays(
                tetris,
                """
                -
                -
                -
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
                -
                -
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
                -
                #
                -
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
                -
                -
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
                -
                -
                -
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
                -
                -
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
                -
                -
                -
                """,
                2
            )
        }
    }

    @Nested
    inner class `with two columns should` {
        private var tetris = Tetris(TetrisFrame(2, 3), listOf(Structure.create1by1()), 0)

        @Test
        fun `start empty`() {
            assertDisplays(
                tetris,
                """
                --
                --
                --
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
                --
                --
                #-
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
                #-
                --
                #-
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
                --
                #-
                #-
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
                #-
                #-
                #-
                """,
                0
            )
            assertThat(tetris.gameOver).isEqualTo("")
        }

        @Test
        fun `report game over`() {
            tickTimes(tetris, 9)

            assertThat(tetris.gameOver).isEqualTo("Game Over")
        }
    }

    @Nested
    inner class `with three columns should` {
        private var tetris = Tetris(TetrisFrame(3, 3), listOf(Structure.create1by1()), 0)

        @Test
        fun `start empty`() {
            assertDisplays(
                tetris,
                """
                ---
                ---
                ---
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
                -#-
                ---
                ---
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
                -#-
                ---
                -#-
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
                --#
                ---
                ---
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
                ---
                --#
                ---
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
                ---
                ---
                --#
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
                #--
                ---
                ---
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
                #--
                ---
                ---
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
                --#
                ---
                ---
                """,
                0
            )
        }

        @Test
        fun `erases filled line`() {
            tetris.left()
            tickTimes(tetris, 4)

            tetris.right()
            tickTimes(tetris, 4)

            tickTimes(tetris, 4)

            assertDisplays(
                tetris,
                """
                ---
                ---
                ---
                """,
                3
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
                #-#
                #-#
                #-#
                """,
                0
            )
        }
    }

    private fun assertDisplays(tetris: Tetris, output: String, score: Int) {
        assertEquals(
            "\n" + output.trimIndent() + "\n",
            tetris.gameDisplayString()
        )
        assertEquals(
            score,
            tetris.score
        )
    }

}