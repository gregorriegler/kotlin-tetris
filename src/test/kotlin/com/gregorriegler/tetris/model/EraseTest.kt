package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
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
    fun `can erase debris by area`() {
        val debris = Debris("""
            ###
            ###
            ###
        """)
        assertThat(debris.erase(Area("""
            -#-
            ###
            -#-
        """))).isEqualTo(5) // amount of fields removed

        assertThat(debris).isEqualTo(Debris("""
            #-#
            ---
            #-#
        """))
    }

    @Test
    fun `erases a filled line at the bottom`() {
        val debris = Debris("""
            ---
            -#-
            ###
        """)

        val howMany: Int = debris.eraseFilledRows()

        Assertions.assertEquals(3, howMany)
        Assertions.assertEquals(
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

        Assertions.assertEquals(6, howMany)
        Assertions.assertEquals(
            Debris("""
            -#-
            ---
            ---
            """),
            debris
        )
    }

    @Test
    fun `area falls to bottom`() {
        assertThat(Area("""
            #
            -
        """).fall()).isEqualTo(Area("""
            -
            #
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

    //    @Test
//    fun `debris falls`() {
//        val debris = Debris("""
//            #
//            -
//        """)
//
//        debris.fall()
//
//        assertThat(debris).isEqualTo(Debris("""
//            -
//            #
//        """))

}