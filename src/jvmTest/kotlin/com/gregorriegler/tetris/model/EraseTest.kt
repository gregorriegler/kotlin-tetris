package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `Erasing debris` {

    @Test
    fun `can erase grid from grid`() {
        assertThat(
            Eraser(
                Grid(
                    """
                        ###
                        ###
                        ###
                        """
                )
            ).erase(
                """
                        -#-
                        ###
                        -#-
                        """
            )
        ).isEqualTo(
            EraseResult(
                """
                #-#
                ---
                #-#
                """,
                5
            )
        )
    }

    @Test
    fun `erasing coin increases the score`() {
        assertThat(
            Eraser(Grid("O")).erase("#")
        ).isEqualTo(
            EraseResult("-", 10)
        )
    }

    @Test
    fun `can erase soil and coin by grid`() {
        assertThat(
            Eraser(
                Grid(
                    """
                    ###
                    ■O■
                    ###
                    """
                )
            ).erase(
            """
                    -#-
                    ###
                    -#-
                    """
            )
        ).isEqualTo(
            EraseResult(
                """
                #-#
                ---
                #-#
                """,
                14
            )
        )
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

        val score: Int = debris.eraseFilledRows()

        assertEquals(6, score)
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

        val score: Int = debris.eraseFilledRows()

        assertEquals(24, score)
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