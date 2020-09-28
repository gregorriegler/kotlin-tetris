package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Grid` {

    @Test
    fun `can be created from a string`() {
        val grid = Grid(
            """
            ##
            ##
        """
        )

        assertEquals(2, grid.width)
        assertEquals(2, grid.height)
        assertTrue(grid.collidesWith(Field.filled(0, 0)))
        assertTrue(grid.collidesWith(Field.filled(1, 0)))
        assertTrue(grid.collidesWith(Field.filled(0, 1)))
        assertTrue(grid.collidesWith(Field.filled(1, 1)))
        assertFalse(grid.collidesWith(Field.filled(2, 1)))
        assertThat(grid.sizeFalling()).isEqualTo(4)
    }

    @Test
    fun `can be created from a string #2`() {
        val grid = Grid(
            """
            #-
            ##
        """
        )

        assertEquals(2, grid.width)
        assertEquals(2, grid.height)
        assertTrue(grid.collidesWith(Field.filled(0, 0)))
        assertFalse(grid.collidesWith(Field.filled(1, 0)))
        assertTrue(grid.collidesWith(Field.filled(0, 1)))
        assertTrue(grid.collidesWith(Field.filled(1, 1)))
        assertFalse(grid.collidesWith(Field.filled(2, 1)))
        assertThat(grid.sizeFalling()).isEqualTo(3)
    }

    @Test
    fun `can be created from a string #3`() {
        val grid = Grid(
            """
            ---
            -#-
            ---
        """
        )

        assertEquals(3, grid.width)
        assertEquals(3, grid.height)
        assertFalse(grid.collidesWith(Field.filled(0, 0)))
        assertFalse(grid.collidesWith(Field.filled(1, 0)))
        assertFalse(grid.collidesWith(Field.filled(2, 0)))
        assertFalse(grid.collidesWith(Field.filled(0, 1)))
        assertTrue(grid.collidesWith(Field.filled(1, 1)))
        assertFalse(grid.collidesWith(Field.filled(2, 1)))
        assertFalse(grid.collidesWith(Field.filled(0, 2)))
        assertFalse(grid.collidesWith(Field.filled(1, 2)))
        assertFalse(grid.collidesWith(Field.filled(2, 2)))
        assertThat(grid.sizeFalling()).isEqualTo(1)
    }

    @Test
    fun `can be created from a string with bomb`() {
        val grid = Grid(
            """
            X
        """
        )

        assertEquals(1, grid.width)
        assertEquals(1, grid.height)
        assertTrue(grid.collidesWith(Field.filled(0, 0)))
        assertFalse(grid.collidesWith(Field.filled(1, 0)))
        assertFalse(grid.collidesWith(Field.filled(2, 0)))
        assertFalse(grid.collidesWith(Field.filled(0, 1)))
        assertThat(grid.sizeFalling()).isEqualTo(1)
    }

    @Test
    fun `can be indented on the x axis`() {
        val grid = Grid(
            """
            >##
            >##
        """
        )

        assertEquals(2, grid.width)
        assertEquals(2, grid.height)
        assertTrue(grid.collidesWith(Field.filled(1, 0)))
        assertTrue(grid.collidesWith(Field.filled(2, 0)))
        assertTrue(grid.collidesWith(Field.filled(1, 1)))
        assertTrue(grid.collidesWith(Field.filled(2, 1)))
        assertFalse(grid.collidesWith(Field.filled(0, 1)))
    }

    @Test
    fun `can be indented on the y axis`() {
        val grid = Grid(
            """
            >>
            ##
            ##
        """
        )

        assertEquals(2, grid.width)
        assertEquals(2, grid.height)
        assertTrue(grid.collidesWith(Field.filled(0, 1)))
        assertTrue(grid.collidesWith(Field.filled(1, 1)))
        assertTrue(grid.collidesWith(Field.filled(0, 2)))
        assertTrue(grid.collidesWith(Field.filled(1, 2)))
        assertFalse(grid.collidesWith(Field.filled(0, 0)))
    }

    @Test
    fun `gets the state`() {
        assertThat(
            Grid(
                """
            >>
            ##
            ##
        """
            ).state()
        ).isEqualTo(
            listOf(
                listOf(Filling.EMPTY, Filling.EMPTY),
                listOf(Filling.FALLING, Filling.FALLING),
                listOf(Filling.FALLING, Filling.FALLING),
            )
        )
    }

    @Test
    fun `gets fields`() {
        val grid = Grid(
            """
            >>>
            >##
            >##
        """
        )
        assertThat(grid.get(0, 0)).isEqualTo(Field.empty(0, 0))
        assertThat(grid.get(1, 1)).isEqualTo(Field.filled(1, 1))
        assertThat(grid.get(2, 1)).isEqualTo(Field.filled(2, 1))
        assertThat(grid.get(3, 1)).isEqualTo(Field.empty(3, 1))
    }

    @Test
    fun `draws itself`() {
        assertEquals(
            '\n' + """
            --
            ##
            ##
        """.trimIndent() + '\n', Grid(
                """
            >>
            ##
            ##
        """
            ).toString()
        )
    }

    @Test
    fun `has a width`() {
        assertEquals(1, Grid("#").width)
        assertEquals(2, Grid("##").width)
    }

    @Test
    fun `has a height`() {
        assertEquals(1, Grid("#").height)
        assertEquals(2, Grid("#\n#").height)
    }

    @Test
    fun `has a bottom`() {
        assertEquals(0, Grid("#").bottomFalling())
        assertEquals(1, Grid("#\n#").bottomFalling())
    }

    @Test
    fun `has a left side of non-empty`() {
        assertEquals(0, Grid("#").leftSideFalling())
        assertEquals(1, Grid("-##").leftSideFalling())
    }

    @Test
    fun `has a right side`() {
        assertEquals(0, Grid("#").rightSideFalling())
        assertEquals(1, Grid("##").rightSideFalling())
        assertEquals(1, Grid("##-").rightSideFalling())
    }

    @Test
    fun `collides with a field`() {
        assertTrue(Grid("#").collidesWith(Field.filled(0, 0)))
        assertTrue(Grid("-#").collidesWith(Field.filled(1, 0)))
        assertTrue(Grid(">#").collidesWith(Field.filled(1, 0)))
    }

    @Test
    fun `collides with a grid`() {
        assertThat(Grid("#-").collidesWith(Grid("#-"))).isTrue
    }

    @Test
    fun `does not collide with a grid`() {
        assertThat(Grid("#-").collidesWith(Grid("-#"))).isFalse
    }

    @Test
    fun `does not collide with a field`() {
        assertFalse(Grid("#").collidesWith(Field.filled(1, 1)))
        assertFalse(Grid("#").collidesWith(Field.empty(0, 0)))
        assertFalse(Grid("-#").collidesWith(Field.filled(0, 0)))
        assertFalse(Grid("-#").collidesWith(Field.empty(0, 0)))
        assertFalse(Grid(">#").collidesWith(Field.filled(0, 0)))
    }

    @Test
    fun combines() {
        assertThat(Grid("-#\n#-").combine(Grid("#-")))
            .isEqualTo(Grid("##\n#-"))
    }

    @Test
    fun `combines empty rows`() {
        assertThat(Grid(">\n-").combine(Grid("-")))
            .isEqualTo(Grid("-\n-"))
    }

    @Test
    fun `erases filled rows`() {
        assertThat(Eraser(Grid("#")).eraseFilledRows()).isEqualTo(EraseResult(Grid("-"), 1))

        assertThat(
            Eraser(
                Grid(
                    """
                    >
                    #
                """
                )
            ).eraseFilledRows()
        ).isEqualTo(
            EraseResult(
                Grid(
                    """
                    >
                    -
                    """
                ), 1
            )
        )

        assertThat(
            Eraser(
                Grid(
                    """
                    -
                    #
                    """
                )
            ).eraseFilledRows()
        ).isEqualTo(
            EraseResult(
                Grid(
                    """
                    -
                    -
                    """
                ), 1
            )
        )

        assertThat(
            Eraser(
                Grid(
                    """
                    #-
                    ##
                    #-
                    ##
                """
                )
            ).eraseFilledRows()
        ).isEqualTo(
            EraseResult(
                Grid(
                    """
            #-
            --
            #-
            --
        """
                ), 4
            )
        )
    }

    @Test
    fun `moves down`() {
        assertThat(
            Grid(
                """
                -##
            """
            ).down()
        ).isEqualTo(
            Grid(
                """
                >>>
                -##
            """
            )
        )
    }

    @Test
    fun `moves left`() {
        assertThat(Grid("-##").left()).isEqualTo(Grid("<-##"))
        assertThat(Grid("--#").leftBy(2)).isEqualTo(Grid("<<--#"))
    }

    @Test
    fun `moves right`() {
        assertThat(Grid("##-").right()).isEqualTo(Grid(">##-"))
    }

    @Test
    fun rotates() {
        assertThat(
            Grid(
                """
                ---
                ###
                ---
                """
            ).rotate()
        )
            .isEqualTo(
                Grid(
                    """
                -#-
                -#-
                -#-
                """
                )
            )

        assertThat(
            Grid(
                """
                ---
                ###
                -#-
                """
            ).rotate()
        )
            .isEqualTo(
                Grid(
                    """
                -#-
                ##-
                -#-
                """
                )
            )

        assertThat(
            Grid(
                """
                >>>
                >>>
                ##-
                #--
                #--
                """
            ).rotate()
        )
            .isEqualTo(
                Grid(
                    """
                >>>
                >>>
                ###
                --#
                ---
                """
                )
            )
    }

    @Test
    fun `keeps position on rotating`() {
        assertThat(
            Grid(
                """
                ---
                ###
                ---
                """
            ).rotate().rotate()
        )
            .isEqualTo(
                Grid(
                    """
                ---
                ###
                ---
                """
                )
            )

        assertThat(
            Grid(
                """
                -----
                -----
                #####
                -----
                -----
                """
            ).rotate().rotate()
        )
            .isEqualTo(
                Grid(
                    """
                -----
                -----
                #####
                -----
                -----
                """
                )
            )

        assertThat(
            Grid(
                """
                --#--
                --#--
                -###-
                --#--
                --#--
                """
            ).rotate().rotate()
        )
            .isEqualTo(
                Grid(
                    """
                --#--
                --#--
                -###-
                --#--
                --#--
                """
                )
            )
    }

    @Test
    fun `returns the starting grid for a structure`() {
        assertEquals(Grid(Field.filled(1, -1)), Structure("#").startingPosition(Grid(TetrisFrame(3, 3))))
        assertEquals(Grid(Field.filled(1, -1)), Structure("#").startingPosition(Grid(TetrisFrame(4, 4))))
        assertEquals(
            Grid(Field.filled(1, -2), Field.filled(1, -1)),
            Structure("#\n#").startingPosition(Grid(TetrisFrame(3, 3)))
        )
        assertEquals(
            Grid(Field.filled(1, -1), Field.filled(2, -1)),
            Structure("##").startingPosition(Grid(TetrisFrame(4, 4)))
        )
    }
}

