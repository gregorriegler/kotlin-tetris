package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Frame` {

    @Test
    fun `returns the top center field`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(Field.filled(1, 0), frame.topCenterFilled())
    }

    @Test
    fun `moves a grid to the left`() {
        val frame = TetrisFrame(3, 3)
        val grid1 = Debris(TetrisFrame(3, 3))
        assertEquals(
            Grid(Field.empty(-1, 0), Field.filled(0, 0), Field.filled(1, 0)),
            frame.left(
                Grid("""
                -##
                """),
                grid1.grid
            )
        )
    }

    @Test
    fun `respects the left wall when moving left`() {
        val frame = TetrisFrame(3, 3)
        val grid1 = Debris(TetrisFrame(3, 3))
        assertEquals(
            Grid("""
                ##-
                """
            ),
            frame.left(
                Grid("""
                ##-
                """
                ),
                grid1.grid
            )
        )
    }

    @Test
    fun `respects debris when moving left`() {
        val frame = TetrisFrame(3, 3)
        val grid = Grid("""
                -##
                """)
        val debris = Debris("""
                #--
                """)
        assertEquals(grid, frame.left(grid, debris.grid))
    }

    @Test
    fun `moves inside debris when moving left`() {
        val frame = TetrisFrame(4, 3)
        val grid = Grid("""
                ---#
                --##
                ---#
                """)
        val debris = Debris("""
                ##--
                #---
                ##--
                """)

        val x = -1
        val y = 0
        val expected = Grid(
            Field.empty(x, y),
            Field.empty(0, 0),
            Field.empty(1, 0),
            Field.filled(2, 0),
            Field.empty(-1, 1),
            Field.empty(0, 1),
            Field.filled(1, 1),
            Field.filled(2, 1),
            Field.empty(-1, 2),
            Field.empty(0, 2),
            Field.empty(1, 2),
            Field.filled(2, 2),
        )
        val actual = frame.left(grid, debris.grid)
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun `moves a grid to the right`() {
        val frame = TetrisFrame(3, 3)
        val obstacle = Debris(frame)
        assertThat(
            frame.right(Grid("##-"), obstacle.grid)
        ).isEqualTo(Grid(">##-"))
    }

    @Test
    fun `respects the right wall when moving right`() {
        val frame = TetrisFrame(2, 2)
        val obstacle = Debris(frame)
        assertEquals(
            Grid("""
                -#
                -#
                """
            ),
            frame.right(
                Grid("""
                -#
                -#
                """
                ),
                obstacle.grid
            )
        )
    }

    @Test
    fun `respects debris when moving right`() {
        val frame = TetrisFrame(3, 3)
        val obstacle = Debris(
            """
                --#
                --#
                --#
                """
        )
        assertEquals(
            Grid("""
                ---
                ##-
                """
            ),
            frame.right(
                Grid("""
                ---
                ##-
                """
                ),
                obstacle.grid
            ))
    }

    @Test
    fun `moves inside debris when moving right`() {
        val frame = TetrisFrame(4, 3)
        val grid = Grid("""
                #---
                ##--
                #---
                """)
        val debris = Debris("""
                --##
                ---#
                --##
                """)

        assertEquals(
            Grid("""
                >#---
                >##--
                >#---
                """),
            frame.right(grid, debris.grid)
        )
    }

    @Test
    fun `moves a grid down`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(
            Grid("""
                >>
                ##
                --
                """
            ),
            frame.down(
                Grid("""
                    ##
                    --
                    """
                )
            )
        )
    }

    @Test
    fun `does not move a grid below the bottom`() {
        val frame = TetrisFrame(2, 2)
        assertEquals(
            Grid("""
                --
                ##
                """
            ),
            frame.down(
                Grid("""
                --
                ##
                """
                )
            )
        )
    }

    @Test
    fun `knows a grid is not at the bottom`() {
        assertFalse(TetrisFrame(3, 3).isAtBottom(Grid(Field.filled(1, 1))))
        assertFalse(
            TetrisFrame(4, 4).isAtBottom(
                Grid("""
                    >>
                    ##
                    ##
                """.trimIndent())
            )
        )
    }

    @Test
    fun `knows a grid is at the bottom`() {
        assertTrue(TetrisFrame(2, 2).isAtBottom(Grid(Field.filled(1, 1))))
        assertTrue(
            TetrisFrame(4, 4).isAtBottom(
                Grid("""
                    >>
                    >>
                    ##
                    ##
                """.trimIndent())
            )
        )
    }
}

class FrameTest {
    @Test
    fun `creates max that fits within height`() {
        val max = SimpleFrame.max(SimpleFrame(20, 100), SimpleFrame(200, 200))
        assertThat(max).isEqualTo(SimpleFrame(40, 200))
    }
}