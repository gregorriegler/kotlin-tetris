package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Frame` {

    @Test
    fun `returns the top center field`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(Field.filled(MovablePosition(1, 0)), frame.topCenterFilled())
    }

    @Test
    fun `moves an area to the left`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(
            Area(Field.empty(MovablePosition(-1, 0)), Field.filled(MovablePosition(0, 0)), Field.filled(MovablePosition(1, 0))),
            frame.left(
                Area("""
                -##
                """),
                Debris(TetrisFrame(3, 3))
            )
        )
    }

    @Test
    fun `respects the left wall when moving left`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(
            Area("""
                ##-
                """
            ),
            frame.left(
                Area("""
                ##-
                """
                ),
                Debris(TetrisFrame(3, 3))
            )
        )
    }

    @Test
    fun `respects debris when moving left`() {
        val frame = TetrisFrame(3, 3)
        val area = Area("""
                -##
                """)
        val debris = Debris("""
                #--
                """)
        assertEquals(area, frame.left(area, debris))
    }

    @Test
    fun `moves inside debris when moving left`() {
        val frame = TetrisFrame(4, 3)
        val area = Area("""
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
        val expected = Area(
            Field.empty(MovablePosition(x, y)),
            Field.empty(MovablePosition(0, 0)),
            Field.empty(MovablePosition(1, 0)),
            Field.filled(MovablePosition(2, 0)),
            Field.empty(MovablePosition(-1, 1)),
            Field.empty(MovablePosition(0, 1)),
            Field.filled(MovablePosition(1, 1)),
            Field.filled(MovablePosition(2, 1)),
            Field.empty(MovablePosition(-1, 2)),
            Field.empty(MovablePosition(0, 2)),
            Field.empty(MovablePosition(1, 2)),
            Field.filled(MovablePosition(2, 2)),
        )
        val actual = frame.left(area, debris)
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun `moves an area to the right`() {
        val frame = TetrisFrame(3, 3)
        assertThat(
            frame.right(Area("##-"), Debris(frame))
        ).isEqualTo(Area(">##-"))
    }

    @Test
    fun `respects the right wall when moving right`() {
        val frame = TetrisFrame(2, 2)
        assertEquals(
            Area("""
                -#
                -#
                """
            ),
            frame.right(
                Area("""
                -#
                -#
                """
                ),
                Debris(frame)
            )
        )
    }

    @Test
    fun `respects debris when moving right`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(
            Area("""
                ---
                ##-
                """
            ),
            frame.right(
                Area("""
                ---
                ##-
                """
                ),
                Debris("""
                --#
                --#
                --#
                """)
            ))
    }

    @Test
    fun `moves inside debris when moving right`() {
        val frame = TetrisFrame(4, 3)
        val area = Area("""
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
            Area("""
                >#---
                >##--
                >#---
                """),
            frame.right(area, debris)
        )
    }

    @Test
    fun `moves an area down`() {
        val frame = TetrisFrame(3, 3)
        assertEquals(
            Area("""
                >>
                ##
                --
                """
            ),
            frame.down(
                Area("""
                    ##
                    --
                    """
                )
            )
        )
    }

    @Test
    fun `does not move an area below the bottom`() {
        val frame = TetrisFrame(2, 2)
        assertEquals(
            Area("""
                --
                ##
                """
            ),
            frame.down(
                Area("""
                --
                ##
                """
                )
            )
        )
    }

    @Test
    fun `knows an area is not at the bottom`() {
        assertFalse(TetrisFrame(3, 3).isAtBottom(Area(Field.filled(MovablePosition(1, 1)))))
        assertFalse(TetrisFrame(4, 4).isAtBottom(Area("""
            >>
            ##
            ##
        """.trimIndent())))
    }

    @Test
    fun `knows an area is at the bottom`() {
        assertTrue(TetrisFrame(2, 2).isAtBottom(Area(Field.filled(MovablePosition(1, 1)))))
        assertTrue(TetrisFrame(4, 4).isAtBottom(Area("""
            >>
            >>
            ##
            ##
        """.trimIndent())))
    }
}