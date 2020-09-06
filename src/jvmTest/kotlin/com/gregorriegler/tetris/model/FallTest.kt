package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FallTest {
    @Test
    fun `area falls to bottom`() {
        assertThat(
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
        assertThat(
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
        assertThat(
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
        assertThat(Area("#").fall()).isEqualTo(Area("#"))
    }

    @Test
    fun `fields that have an anchor don't fall`() {
        assertThat(
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
        assertThat(
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
    fun `anchor found on zig zags`() {
        assertThat(
            Area(
                """
                ##
                -#
                ##
                #-
                """
            ).fall()
        ).isEqualTo(
            Area(
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
            Area(
                """
                ###
                #-#
                --#
                """
            ).fall()
        ).isEqualTo(
            Area(
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
        assertThat(
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

        assertThat(
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