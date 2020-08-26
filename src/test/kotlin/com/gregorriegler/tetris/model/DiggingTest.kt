package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `DiggingTest` {
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
            ###
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
            ###
        """))
    }
}