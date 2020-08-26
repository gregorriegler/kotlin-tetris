package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Digging {

    @Test
    fun `can create soil`() {
        val soil = Field.soil(0, 3)
        assertThat(soil.filling).isEqualTo(Filling.SOIL)
        assertThat(soil.x).isEqualTo(0)
        assertThat(soil.y).isEqualTo(3)
    }

    @Test
    fun can_dig_a_debris() {
        val debris = Debris("""
            ---
            -#-
            ###
        """)

        debris.dig(1)

        assertThat(debris).isEqualTo(Debris("""
            -#-
            ###
            ■■■
        """))
    }

    @Test
    fun can_dig_an_area() {
        assertThat(Area("""
            ---
            -#-
            ###
        """).dig(1)).isEqualTo(Area("""
            -#-
            ###
            ■■■
        """))
    }
}