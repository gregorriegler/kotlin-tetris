package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SoilTest {

    @Test
    fun `can create soil`() {
        val soil = Field.soil(0, 3)
        assertThat(soil.filling).isEqualTo(Filling.SOIL)
        assertThat(soil.x).isEqualTo(0)
        assertThat(soil.y).isEqualTo(3)
    }

    @Test
    fun can_dig_an_area() {
        assertThat(Area("""
            ---
            ---
            ---
        """).digForRowsOfSoil(1)).isEqualTo(Area("""
            ---
            ---
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ---
        """).digForRowsOfSoil(2)).isEqualTo(Area("""
            ---
            ■■■
            ■■■
        """))
    }

    @Test
    fun can_dig_partly_filled_area() {
        assertThat(Area("""
            ---
            ---
            ■--
        """).digForRowsOfSoil(1)).isEqualTo(Area("""
            ---
            ■--
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ■--
        """).digForRowsOfSoil(2)).isEqualTo(Area("""
            ■--
            ■■■
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            #--
        """).digForRowsOfSoil(1)).isEqualTo(Area("""
            ---
            #--
            ■■■
        """))
    }

    @Test
    fun can_dig_a_debris() {
        val debris = Debris("""
            ---
            ■#-
            ■■■
        """)

        debris.dig(2)

        assertThat(debris).isEqualTo(Debris("""
            ■#-
            ■■■
            ■■■
        """))
    }
}