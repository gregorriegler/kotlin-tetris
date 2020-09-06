package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FallTest {
    @Test
    fun `area falls to bottom`() {
        Assertions.assertThat(
            Area(
                """
                #
                -
                """
            ).fall().fall()
        ).isEqualTo(
            Area(
                """
                -
                #
                """
            )
        )
    }

    @Test
    fun `area falls on soil`() {
        Assertions.assertThat(
            Area(
                """
                #
                -
                ■
                """
            ).fall().fall()
        ).isEqualTo(
            Area(
                """
                -
                #
                ■
                """
            )
        )
    }

    @Test
    fun `area falls by just one field`() {
        Assertions.assertThat(
            Area(
                """
                #
                -
                -
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                -
                #
                -
                """
            )
        )
    }

    @Test
    fun `area on bottom does not fall any further`() {
        Assertions.assertThat(Area("#").fall()).isEqualTo(Area("#"))
    }

    @Test
    fun `fields that have an anchor don't fall`() {
        Assertions.assertThat(
            Area(
                """
                ##
                #-
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                ##
                #-
                """
            )
        )
    }

    @Test
    fun `soil counts as anchor`() {
        Assertions.assertThat(
            Area(
                """
                ##
                #-
                ■-
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                ##
                #-
                ■-
                """
            )
        )
    }

    @Test
    fun `whole lines fall`() {
        Assertions.assertThat(
            Area(
                """
                ##
                --
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                --
                ##
                """
            )
        )
    }

    @Test
    fun `debris falls together`() {
        Assertions.assertThat(
            Area(
                """
                #
                #
                -
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                -
                #
                #
                """
            )
        )

        Assertions.assertThat(
            Area(
                """
                ###
                ###
                ---
                """
            ).fall()
        ).isEqualTo(
            Area(
                """
                ---
                ###
                ###
                """
            )
        )
    }
}