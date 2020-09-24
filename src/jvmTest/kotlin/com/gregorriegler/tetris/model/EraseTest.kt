package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
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
    fun `can erase soil and coin by area`() {
        val erased = Area(
            """
            ###
            ■O■
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
    fun `erases soil as well as coin below`() {
        val debris = Debris(
            """
            -#-
            ###
            OO■
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

    @Test
    fun `does not erase a line of coins`() {
        val debris = Debris(
            """
            -#-
            OOO
        """
        )

        val howMany: Int = debris.eraseFilledRows()

        assertEquals(0, howMany)
        assertEquals(
            Debris(
                """
            -#-
            OOO
        """
            ),
            debris
        )
    }
}