package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `Erasing debris` {

    @Test
    fun `can erase area by area`() {
        val erased = Area("""
            ###
            ###
            ###
        """).erase(Area("""
            >#>
            ###
            >#>
        """))
        val expected = Area("""
            #>#
            >>>
            #>#
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
            >#>
            ###
            >#>
        """))).isEqualTo(5) // amount of fields removed

        assertThat(debris).isEqualTo(Debris("""
            #>#
            >>>
            #>#
        """))
    }
}