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
        assertTrue(area.collides(Field.filled(0, 0)))
        assertTrue(area.collides(Field.filled(1, 0)))
        assertTrue(area.collides(Field.filled(0, 1)))
        assertTrue(area.collides(Field.filled(1, 1)))
        assertFalse(area.collides(Field.filled(2, 1)))
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
        assertTrue(area.collides(Field.filled(0, 0)))
        assertFalse(area.collides(Field.filled(1, 0)))
        assertTrue(area.collides(Field.filled(0, 1)))
        assertTrue(area.collides(Field.filled(1, 1)))
        assertFalse(area.collides(Field.filled(2, 1)))
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
        assertFalse(area.collides(Field.filled(0, 0)))
        assertFalse(area.collides(Field.filled(1, 0)))
        assertFalse(area.collides(Field.filled(2, 0)))
        assertFalse(area.collides(Field.filled(0, 1)))
        assertTrue(area.collides(Field.filled(1, 1)))
        assertFalse(area.collides(Field.filled(2, 1)))
        assertFalse(area.collides(Field.filled(0, 2)))
        assertFalse(area.collides(Field.filled(1, 2)))
        assertFalse(area.collides(Field.filled(2, 2)))
    }

    @Test
    fun `can be indented on the x axis`() {
        val area = Area("""
            >##
            >##
        """)

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.collides(Field.filled(1, 0)))
        assertTrue(area.collides(Field.filled(2, 0)))
        assertTrue(area.collides(Field.filled(1, 1)))
        assertTrue(area.collides(Field.filled(2, 1)))
        assertFalse(area.collides(Field.filled(0, 1)))
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
        assertTrue(area.collides(Field.filled(0, 1)))
        assertTrue(area.collides(Field.filled(1, 1)))
        assertTrue(area.collides(Field.filled(0, 2)))
        assertTrue(area.collides(Field.filled(1, 2)))
        assertFalse(area.collides(Field.filled(0, 0)))
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
        assertEqualsMultilineString("""
            --
            ##
            ##
        """, Area("""
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
        assertEquals(0, Area("#").bottomOfFilled())
        assertEquals(1, Area("#\n#").bottomOfFilled())
    }

    @Test
    fun `has a left side`() {
        assertEquals(0, Area("#").leftSideOfFilled())
        assertEquals(1, Area("-##").leftSideOfFilled())
    }

    @Test
    fun `has a right side`() {
        assertEquals(0, Area("#").rightSideOfFilled())
        assertEquals(1, Area("##").rightSideOfFilled())
    }

    @Test
    fun `collides with a field`() {
        assertTrue(Area("#").collides(Field.filled(0, 0)))
        assertTrue(Area("-#").collides(Field.filled(1, 0)))
        assertTrue(Area(">#").collides(Field.filled(1, 0)))
    }

    @Test
    fun `does not collide with a field`() {
        assertFalse(Area("#").collides(Field.filled(1, 1)))
        assertFalse(Area("#").collides(Field.empty(0,0)))
        assertFalse(Area("-#").collides(Field.filled(0, 0)))
        assertFalse(Area("-#").collides(Field.empty(0, 0)))
        assertFalse(Area(">#").collides(Field.filled(0, 0)))
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
    fun collides() {
        assertThat(Area("#-").collides(Area("#-"))).isTrue
    }

    @Test
    fun `does not collide`() {
        assertThat(Area("#-").collides(Area("-#"))).isFalse
    }

    @Test
    fun `removes filled lines`() {
        assertThat(Area("""
            >
            #
        """).dissolveFilledRows()).isEqualTo(
            Pair(Area("""
                >
                -
            """.trimIndent()), 1))

        assertThat(Area("""
            #
        """).dissolveFilledRows()).isEqualTo(
            Pair(Area("""
            -
        """), 1))

        assertThat(Area("""
            -
            #
        """).dissolveFilledRows()).isEqualTo(
            Pair(Area("""
            -
            -
        """), 1))

        assertThat(Area("""
            #-
            ##
            #-
            ##
        """).dissolveFilledRows()).isEqualTo(Pair(Area("""
            --
            --
            #-
            #-
        """), 2))
    }

    @Test
    fun `moves down`() {
        assertThat(
            Area("""
                -##
            """).down()
        ).isEqualTo(
            Area("""
                >>>
                -##
            """)
        )
    }

    @Test
    fun `moves left`() {
        assertThat(Area("-##").left()).isEqualTo(Area(
            Field.empty(-1, 0), Field.filled(0, 0), Field.filled(1, 0)
        ))
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

    private fun assertEqualsMultilineString(expected: String, actual: String) {
        assertEquals('\n' + expected.trimIndent() + '\n', actual)
    }
}

