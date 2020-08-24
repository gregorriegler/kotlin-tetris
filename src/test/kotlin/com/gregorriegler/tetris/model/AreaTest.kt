package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `An Area` {

    @Test
    fun `can be created from a string`() {
        val area = Area("""
            ##
            ##
        """
        )

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.collidesWith(Field.filled(0, 0)))
        assertTrue(area.collidesWith(Field.filled(1, 0)))
        assertTrue(area.collidesWith(Field.filled(0, 1)))
        assertTrue(area.collidesWith(Field.filled(1, 1)))
        assertFalse(area.collidesWith(Field.filled(2, 1)))
        assertThat(area.size()).isEqualTo(4)
    }

    @Test
    fun `can be created from a string #2`() {
        val area = Area("""
            #-
            ##
        """
        )

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.collidesWith(Field.filled(0, 0)))
        assertFalse(area.collidesWith(Field.filled(1, 0)))
        assertTrue(area.collidesWith(Field.filled(0, 1)))
        assertTrue(area.collidesWith(Field.filled(1, 1)))
        assertFalse(area.collidesWith(Field.filled(2, 1)))
        assertThat(area.size()).isEqualTo(3)
    }

    @Test
    fun `can be created from a string #3`() {
        val area = Area("""
            ---
            -#-
            ---
        """
        )

        assertEquals(3, area.width())
        assertEquals(3, area.height())
        assertFalse(area.collidesWith(Field.filled(0, 0)))
        assertFalse(area.collidesWith(Field.filled(1, 0)))
        assertFalse(area.collidesWith(Field.filled(2, 0)))
        assertFalse(area.collidesWith(Field.filled(0, 1)))
        assertTrue(area.collidesWith(Field.filled(1, 1)))
        assertFalse(area.collidesWith(Field.filled(2, 1)))
        assertFalse(area.collidesWith(Field.filled(0, 2)))
        assertFalse(area.collidesWith(Field.filled(1, 2)))
        assertFalse(area.collidesWith(Field.filled(2, 2)))
        assertThat(area.size()).isEqualTo(1)
    }

    @Test
    fun `can be created from a string with bomb`() {
        val area = Area("""
            X
        """
        )

        assertEquals(1, area.width())
        assertEquals(1, area.height())
        assertTrue(area.collidesWith(Field.filled(0, 0)))
        assertFalse(area.collidesWith(Field.filled(1, 0)))
        assertFalse(area.collidesWith(Field.filled(2, 0)))
        assertFalse(area.collidesWith(Field.filled(0, 1)))
        assertThat(area.size()).isEqualTo(1)
    }

    @Test
    fun `can be indented on the x axis`() {
        val area = Area("""
            >##
            >##
        """)

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.collidesWith(Field.filled(1, 0)))
        assertTrue(area.collidesWith(Field.filled(2, 0)))
        assertTrue(area.collidesWith(Field.filled(1, 1)))
        assertTrue(area.collidesWith(Field.filled(2, 1)))
        assertFalse(area.collidesWith(Field.filled(0, 1)))
    }

    @Test
    fun `can be indented on the y axis`() {
        val area = Area("""
            >>
            ##
            ##
        """)

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.collidesWith(Field.filled(0, 1)))
        assertTrue(area.collidesWith(Field.filled(1, 1)))
        assertTrue(area.collidesWith(Field.filled(0, 2)))
        assertTrue(area.collidesWith(Field.filled(1, 2)))
        assertFalse(area.collidesWith(Field.filled(0, 0)))
    }

    @Test
    fun `gets the state`() {
        assertThat(Area("""
            >>
            ##
            ##
        """).state()).isEqualTo(
            listOf(
                listOf(Filling.EMPTY, Filling.EMPTY),
                listOf(Filling.FILLED, Filling.FILLED),
                listOf(Filling.FILLED, Filling.FILLED),
            )
        )
    }

    @Test
    fun `draws itself`() {
        assertEquals('\n' + """
            --
            ##
            ##
        """.trimIndent() + '\n', Area("""
            >>
            ##
            ##
        """).toString())
    }

    @Test
    fun `has a width`() {
        assertEquals(1, Area("#").width())
        assertEquals(2, Area("##").width())
    }

    @Test
    fun `has a height`() {
        assertEquals(1, Area("#").height())
        assertEquals(2, Area("#\n#").height())
    }

    @Test
    fun `has a bottom`() {
        assertEquals(0, Area("#").bottomNonEmpty())
        assertEquals(1, Area("#\n#").bottomNonEmpty())
    }

    @Test
    fun `has a left side of non-empty`() {
        assertEquals(0, Area("#").leftSideNonEmpty())
        assertEquals(1, Area("-##").leftSideNonEmpty())
    }

    @Test
    fun `has a right side`() {
        assertEquals(0, Area("#").rightSideNonEmpty())
        assertEquals(1, Area("##").rightSideNonEmpty())
        assertEquals(1, Area("##-").rightSideNonEmpty())
    }

    @Test
    fun `collides with a field`() {
        assertTrue(Area("#").collidesWith(Field.filled(0, 0)))
        assertTrue(Area("-#").collidesWith(Field.filled(1, 0)))
        assertTrue(Area(">#").collidesWith(Field.filled(1, 0)))
    }

    @Test
    fun `collides with an area`() {
        assertThat(Area("#-").collidesWith(Area("#-"))).isTrue
    }

    @Test
    fun `does not collide with an area`() {
        assertThat(Area("#-").collidesWith(Area("-#"))).isFalse
    }

    @Test
    fun `does not collide with a field`() {
        assertFalse(Area("#").collidesWith(Field.filled(1, 1)))
        assertFalse(Area("#").collidesWith(Field.empty(0, 0)))
        assertFalse(Area("-#").collidesWith(Field.filled(0, 0)))
        assertFalse(Area("-#").collidesWith(Field.empty(0, 0)))
        assertFalse(Area(">#").collidesWith(Field.filled(0, 0)))
    }

    @Test
    fun combines() {
        assertThat(Area("-#\n#-").combine(Area("#-")))
            .isEqualTo(Area("##\n#-"))
    }

    @Test
    fun `combines empty rows`() {
        assertThat(Area(">\n-").combine(Area("-")))
            .isEqualTo(Area("-\n-"))
    }

    @Test
    fun `retrieves filled rows`() {
        assertThat(Area("#").filledRows()).isEqualTo(Area("#"))
    }

    @Test
    fun `dissolves filled rows`() {
        assertThat(Area("#").eraseFilledRowsNew()).isEqualTo(Pair(Area("-"), 1))

        assertThat(Area("""
            >
            #
        """).eraseFilledRowsNew()).isEqualTo(
            Pair(Area("""
            >
            -
            """.trimIndent()), 1))

        assertThat(Area("""
            -
            #
        """).eraseFilledRowsNew()).isEqualTo(
            Pair(Area("""
            -
            -
        """), 1))

        assertThat(Area("""
            #-
            ##
            #-
            ##
        """).eraseFilledRowsNew()).isEqualTo(Pair(Area("""
            #-
            --
            #-
            --
        """), 4))
    }

    @Test
    fun `moves down`() {
        assertThat(Area("""
                -##
            """).down()
        ).isEqualTo(Area("""
                >>>
                -##
            """))
    }

    @Test
    fun `moves left`() {
        assertThat(Area("-##").left()).isEqualTo(Area("<-##"))
        assertThat(Area("--#").left(2)).isEqualTo(Area("<<--#"))
    }

    @Test
    fun `moves right`() {
        assertThat(Area("##-").right()).isEqualTo(Area(">##-"))
    }

    @Test
    fun rotates() {
        assertThat(Area("""
                ---
                ###
                ---
                """).rotate())
            .isEqualTo(Area("""
                -#-
                -#-
                -#-
                """))

        assertThat(Area("""
                ---
                ###
                -#-
                """).rotate())
            .isEqualTo(Area("""
                -#-
                ##-
                -#-
                """))

        assertThat(Area("""
                >>>
                >>>
                ##-
                #--
                #--
                """).rotate())
            .isEqualTo(Area("""
                >>>
                >>>
                ###
                --#
                ---
                """))
    }

    @Test
    fun `keeps position on rotating`() {
        assertThat(Area("""
                ---
                ###
                ---
                """).rotate().rotate())
            .isEqualTo(Area("""
                ---
                ###
                ---
                """))

        assertThat(Area("""
                -----
                -----
                #####
                -----
                -----
                """).rotate().rotate())
            .isEqualTo(Area("""
                -----
                -----
                #####
                -----
                -----
                """))

        assertThat(Area("""
                --#--
                --#--
                -###-
                --#--
                --#--
                """).rotate().rotate())
            .isEqualTo(Area("""
                --#--
                --#--
                -###-
                --#--
                --#--
                """))
    }

    @Test
    fun `returns the starting area for a structure`() {
        assertEquals(Area(Field.filled(1, -1)), Structure("#").aboveCentered(Area(Frame(3, 3))))
        assertEquals(Area(Field.filled(1, -1)), Structure("#").aboveCentered(Area(Frame(4, 4))))
        assertEquals(Area(Field.filled(1, -2), Field.filled(1, -1)), Structure("#\n#").aboveCentered(Area(Frame(3, 3))))
        assertEquals(Area(Field.filled(1, -1), Field.filled(2, -1)), Structure("##").aboveCentered(Area(Frame(4, 4))))
    }
}

