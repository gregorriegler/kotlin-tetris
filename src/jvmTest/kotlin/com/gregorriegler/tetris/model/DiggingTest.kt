package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DiggingTest {

    @Test
    fun `can create soil`() {
        val soil = Field.soil(Position(0, 3))
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
        """).dig(1)).isEqualTo(Area("""
            ---
            ---
            ■■■
        """))

        // only dig one row at a time
        assertThat(Area("""
            ---
            ---
            ---
        """).dig(2)).isEqualTo(Area("""
            ---
            ---
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ---
        """).dig(2).dig(2)).isEqualTo(Area("""
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
        """).dig(1)).isEqualTo(Area("""
            ---
            ■--
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ■--
        """).dig(2).dig(2)).isEqualTo(Area("""
            ■--
            ■■■
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            #--
        """).dig(1)).isEqualTo(Area("""
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
        debris.dig(2)

        assertThat(debris).isEqualTo(Debris("""
            ■#-
            ■■■
            ■■■
        """))
    }
}