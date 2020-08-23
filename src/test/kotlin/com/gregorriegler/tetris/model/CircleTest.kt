package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A circle` {

    @Test
    fun `can be created`() {
        assertThat(Area.circle(Field(1, 1), 1)).isEqualTo(Area(
            """
            >#>
            ###
            >#>
            """))

        assertThat(Area.circle(Field(6, 6), 6)).isEqualTo(Area(
            """
            >>>>>>#
            >>>#######
            >>#########
            >###########
            >###########
            >###########
            #############
            >###########
            >###########
            >###########
            >>#########
            >>>#######
            >>>>>>#
            """))
    }
}