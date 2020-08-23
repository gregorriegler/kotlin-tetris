package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions
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
        Assertions.assertThat(erased).isEqualTo(expected)
    }

    @Test
    fun `can erase debris by area`() {
        val debris = Debris("""
            ###
            ###
            ###
        """)
        debris.erase(Area("""
            >#>
            ###
            >#>
        """))
        Assertions.assertThat(debris).isEqualTo(Debris("""
            #>#
            >>>
            #>#
        """))
    }
}