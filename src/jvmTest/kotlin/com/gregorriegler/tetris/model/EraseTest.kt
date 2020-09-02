package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Erasing debris` {

    @Test
    fun `can erase area by area`() {
        val erased = Area("""
            ###
            ###
            ###
        """).erase(Area("""
            -#-
            ###
            -#-
        """))
        val expected = Area("""
            #-#
            ---
            #-#
        """)
        assertThat(erased).isEqualTo(expected)
    }

    @Test
    fun `erases a filled line at the bottom`() {
        val debris = Debris("""
            ---
            -#-
            ###
        """)

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(3, howMany)
        assertEquals(
            Debris("""
            ---
            -#-
            ---
            """),
            debris
        )
    }

    @Test
    fun `erases two filled lines at the bottom`() {
        val debris = Debris("""
            -#-
            ###
            ###
        """)

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(6, howMany)
        assertEquals(
            Debris("""
            -#-
            ---
            ---
            """),
            debris
        )
    }

    @Test
    fun `erases soil below as well`() {
        val debris = Debris("""
            -#-
            ###
            ■■■
        """)

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(3, howMany)
        assertEquals(
            Debris("""
            -#-
            ---
            ---
            """),
            debris
        )
    }


    @Test
    fun `erases two lines besides soil`() {
        val debris = Debris("""
            -#-
            ##■
        """)

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(2, howMany)
        assertEquals(
            Debris("""
            -#-
            --■
            """),
            debris
        )
    }

    @Test
    fun `area falls to bottom`() {
        assertThat(Area("""
            #
            -
        """).fall().fall()).isEqualTo(Area("""
            -
            #
        """))
    }

    @Test
    fun `area falls on soil`() {
        assertThat(Area("""
            #
            -
            ■
        """).fall().fall()).isEqualTo(Area("""
            -
            #
            ■
        """))
    }

    @Test
    fun `area falls by just one field`() {
        assertThat(Area("""
            #
            -
            -
        """).fall()).isEqualTo(Area("""
            -
            #
            -
        """))
    }

    @Test
    fun `area on bottom does not fall any further`() {
        assertThat(Area("#").fall()).isEqualTo(Area("#"))
    }

    @Test
    fun `fields that have an anchor don't fall`() {
        assertThat(Area("""
            ##
            #-
        """).fall()).isEqualTo(Area("""
            ##
            #-
            """
        ))
    }

    @Test
    fun `soil counts as anchor`() {
        assertThat(Area("""
            ##
            #-
            ■-
        """).fall()).isEqualTo(Area("""
            ##
            #-
            ■-
            """
        ))
    }

    @Test
    fun `whole lines fall`() {
        assertThat(Area("""
            ##
            --
        """).fall()).isEqualTo(Area("""
            --
            ##
            """
        ))
    }

    @Test
    fun `debris falls together`() {
        assertThat(Area("""
            #
            #
            -
        """).fall()).isEqualTo(Area("""
            -
            #
            #
            """
        ))

        assertThat(Area("""
            ###
            ###
            ---
        """).fall()).isEqualTo(Area("""
            ---
            ###
            ###
            """
        ))
    }
}