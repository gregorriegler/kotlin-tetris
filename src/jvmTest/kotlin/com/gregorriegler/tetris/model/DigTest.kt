package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DigTest {

    @Test
    fun `can create soil`() {
        val soil = Field.soil(0, 3)
        assertThat(soil.filling).isEqualTo(Filling.SOIL)
        assertThat(soil.x).isEqualTo(0)
        assertThat(soil.y).isEqualTo(3)
    }

    @Test
    fun `can create coin`() {
        val coin = Field.coin(0, 3)
        assertThat(coin.filling).isEqualTo(Filling.COIN)
        assertThat(coin.x).isEqualTo(0)
        assertThat(coin.y).isEqualTo(3)
    }

    @Test
    fun `creates a soil when coin percentage is 0`() {
        val field = Field.soilOrCoin(0, 3, 0)
        assertThat(field.filling).isEqualTo(Filling.SOIL)
        assertThat(field.x).isEqualTo(0)
        assertThat(field.y).isEqualTo(3)
    }

    @Test
    fun `creates a coin when coin percentage is 100`() {
        val field = Field.soilOrCoin(0, 3, 100)
        assertThat(field.filling).isEqualTo(Filling.COIN)
        assertThat(field.x).isEqualTo(0)
        assertThat(field.y).isEqualTo(3)
    }

    @Test
    fun `creates a coin about half of the time`() {
        val count = (0 until 100)
            .map { Filling.soilOrCoin(50) }
            .filter { it == Filling.COIN }
            .count()

        assertThat(count).isBetween(30, 70)
    }

    @Test
    fun `can dig a grid`() {
        assertThat(
            Grid(
                """
            ---
            ---
            ---
        """
            ).dig(1, 0).grid
        ).isEqualTo(
            Grid(
                """
            ---
            ---
            ■■■
        """
            )
        )

        // only dig one row at a time
        assertThat(
            Grid(
                """
            ---
            ---
            ---
        """
            ).dig(2, 0).grid
        ).isEqualTo(
            Grid(
                """
            ---
            ---
            ■■■
        """
            )
        )

        assertThat(
            Grid(
                """
            ---
            ---
            ---
        """
            ).dig(2, 0).grid.dig(2, 0).grid
        ).isEqualTo(
            Grid(
                """
            ---
            ■■■
            ■■■
        """
            )
        )
    }

    @Test
    fun `tells how many lines it digged`() {
        assertThat(
            Grid(
                """
            ---
            ---
            ---
        """
            ).dig(1, 0).depth
        ).isEqualTo(1)

        assertThat(
            Grid(
                """
            ---
            ---
            ■■■
        """
            ).dig(1, 0).depth
        ).isEqualTo(0)
    }

    @Test
    fun `digging generates coin`() {
        assertThat(
            Grid(
                """
            ---
            ---
            ---
        """
            ).dig(1, 100).grid
        ).isEqualTo(
            Grid(
                """
            ---
            ---
            OOO
        """
            )
        )
    }

    @Test
    fun `can dig partly filled grid`() {
        assertThat(
            Grid(
                """
            ---
            ---
            ■--
        """
            ).dig(1, 0).grid
        ).isEqualTo(
            Grid(
                """
            ---
            ■--
            ■■■
        """
            )
        )

        assertThat(
            Grid(
                """
            ---
            ---
            ■--
        """
            ).dig(2, 0).grid.dig(2, 0).grid
        ).isEqualTo(
            Grid(
                """
            ■--
            ■■■
            ■■■
        """
            )
        )

        assertThat(
            Grid(
                """
            ---
            ---
            #--
        """
            ).dig(1, 0).grid
        ).isEqualTo(
            Grid(
                """
            ---
            #--
            ■■■
        """
            )
        )
    }

    @Test
    fun `can dig a debris`() {
        val debris = Debris(
            """
            ---
            ■#-
            ■■■
        """
        )

        debris.dig(2)
        debris.dig(2)

        assertThat(debris).isEqualTo(
            Debris(
                """
            ■#-
            ■■■
            ■■■
        """
            )
        )
    }

    @Test
    fun `does not increase depth`() {
        val debris = Debris(
            """
            ---
            ■■■
            ■■■
        """
        )

        debris.dig(2)

        assertThat(debris).isEqualTo(
            Debris(
                """
            ---
            ■■■
            ■■■
        """
            )
        )

        assertThat(debris.depth).isEqualTo(0)
    }

    @Test
    fun `debris starts with 0 depth`() {
        val debris = Debris(
            """
            ---
            ---
            ■--
        """
        )

        assertThat(debris.depth).isEqualTo(0)
    }

    @Test
    fun `digging increases the depth`() {
        val debris = Debris(
            """
            ---
            ---
            ■--
        """
        )

        debris.dig(4)

        assertThat(debris.depth).isEqualTo(1)
    }

    @Test
    fun `digging increases the depth further and further`() {
        val debris = Debris(
            """
            ---
            ---
            ■--
        """
        )

        debris.dig(4)
        debris.dig(4)
        debris.dig(4)

        assertThat(debris.depth).isEqualTo(3)
    }
}