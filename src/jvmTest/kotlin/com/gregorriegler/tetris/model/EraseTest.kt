package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Erasing debris` {

    @Test
    fun `can erase area by area`() {
        val erased = Area(
            """
            ###
            ###
            ###
            """
        ).erase(
            Area(
                """
            -#-
            ###
            -#-
        """
            )
        )
        val expected = Area(
            """
            #-#
            ---
            #-#
        """
        )
        assertThat(erased).isEqualTo(expected)
    }

    @Test
    fun `erases a filled line at the bottom`() {
        val debris = Debris(
            """
            ---
            -#-
            ###
        """
        )

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(3, howMany)
        assertEquals(
            Debris(
                """
            ---
            -#-
            ---
            """
            ),
            debris
        )
    }

    @Test
    fun `erases two filled lines at the bottom`() {
        val debris = Debris(
            """
            -#-
            ###
            ###
        """
        )

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(6, howMany)
        assertEquals(
            Debris(
                """
            -#-
            ---
            ---
            """
            ),
            debris
        )
    }

    @Test
    fun `erases soil below as well`() {
        val debris = Debris(
            """
            -#-
            ###
            ■■■
        """
        )

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(3, howMany)
        assertEquals(
            Debris(
                """
            -#-
            ---
            ---
            """
            ),
            debris
        )
    }


    @Test
    fun `erases two lines besides soil`() {
        val debris = Debris(
            """
            -#-
            ##■
        """
        )

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(2, howMany)
        assertEquals(
            Debris(
                """
            -#-
            --■
            """
            ),
            debris
        )
    }
}