package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A Bomb` {

    @Test
    fun `can create circle`() {
        assertThat(Grid.circle(Position.of(1, 1), 1)).isEqualTo(Grid(
            """
            >#>
            ###
            >#>
            """))

        assertThat(Grid.circle(Position.of(6, 6), 6)).isEqualTo(Grid(
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

    @Test
    fun `bomb explodes`() {
        val debris = Debris("""
            ###############
            ###############
            ###############
            ###############
            #######X#######
            ###############
            ###############
            ###############
            ###############
        """)
        val score = debris.specials()

        assertThat(score).isEqualTo(29)
        assertThat(debris).isEqualTo(Debris("""
            ###############
            #######-#######
            #####-----#####
            #####-----#####
            ####-------####
            #####-----#####
            #####-----#####
            #######-#######
            ###############
        """))
    }
}