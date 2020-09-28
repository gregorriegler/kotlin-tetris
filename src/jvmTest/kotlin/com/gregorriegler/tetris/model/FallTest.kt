package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FallTest {
    @Test
    fun `grid falls to bottom`() {
        assertThat(
            Grid(
                """
                #
                -
                """
            ).fall().fall()
        ).isEqualTo(
            Grid(
                """
                -
                #
                """
            )
        )
    }

    @Test
    fun `grid falls on soil`() {
        assertThat(
            Grid(
                """
                #
                -
                ■
                """
            ).fall().fall()
        ).isEqualTo(
            Grid(
                """
                -
                #
                ■
                """
            )
        )
    }

    @Test
    fun `grid falls by just one field`() {
        assertThat(
            Grid(
                """
                #
                -
                -
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                -
                #
                -
                """
            )
        )
    }

    @Test
    fun `grid on bottom does not fall any further`() {
        assertThat(Grid("#").fall()).isEqualTo(Grid("#"))
    }

    @Test
    fun `fields that have an anchor don't fall`() {
        assertThat(
            Grid(
                """
                ##
                #-
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                ##
                #-
                """
            )
        )
    }

    @Test
    fun `soil counts as anchor`() {
        assertThat(
            Grid(
                """
                ##
                #-
                ■-
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                ##
                #-
                ■-
                """
            )
        )
    }

    @Test
    fun `anchor found on zig zags`() {
        assertThat(
            Grid(
                """
                ##
                -#
                ##
                #-
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                ##
                -#
                ##
                #-
                """
            )
        )
    }

    @Test
    fun `above anchor found`() {
        assertThat(
            Grid(
                """
                ###
                #-#
                --#
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                ###
                #-#
                --#
                """
            )
        )
    }

    @Test
    fun `whole lines fall`() {
        assertThat(
            Grid(
                """
                ##
                --
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                --
                ##
                """
            )
        )
    }

    @Test
    fun `debris falls together`() {
        assertThat(
            Grid(
                """
                #
                #
                -
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                -
                #
                #
                """
            )
        )

        assertThat(
            Grid(
                """
                ###
                ###
                ---
                """
            ).fall()
        ).isEqualTo(
            Grid(
                """
                ---
                ###
                ###
                """
            )
        )
    }
}