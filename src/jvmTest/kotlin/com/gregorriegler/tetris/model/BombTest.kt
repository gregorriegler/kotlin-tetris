package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A Bomb` {

    @Test
    fun `can create circle`() {
        assertThat(Area.circle(SimplePosition(1, 1), 1)).isEqualTo(Area(
            """
            >#>
            ###
            >#>
            """))

        assertThat(Area.circle(SimplePosition(6, 6), 6)).isEqualTo(Area(
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
        debris.specials()

        assertThat(debris).isEqualTo(Debris("""
            #######-#######
            #####-----#####
            ####-------####
            ####-------####
            ###---------###
            ####-------####
            ####-------####
            #####-----#####
            #######-#######
        """))
    }
}