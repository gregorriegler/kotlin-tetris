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
    fun `can dig an area`() {
        assertThat(Area("""
            ---
            ---
            ---
        """).dig(1, 0).area).isEqualTo(Area("""
            ---
            ---
            ■■■
        """))

        // only dig one row at a time
        assertThat(Area("""
            ---
            ---
            ---
        """).dig(2, 0).area).isEqualTo(Area("""
            ---
            ---
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ---
        """).dig(2, 0).area.dig(2, 0).area).isEqualTo(Area("""
            ---
            ■■■
            ■■■
        """))
    }

    @Test
    fun `digging generates coin`() {
        assertThat(Area("""
            ---
            ---
            ---
        """).dig(1, 100).area).isEqualTo(Area("""
            ---
            ---
            OOO
        """))
    }

    @Test
    fun `can dig partly filled area`() {
        assertThat(Area("""
            ---
            ---
            ■--
        """).dig(1, 0).area).isEqualTo(Area("""
            ---
            ■--
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            ■--
        """).dig(2, 0).area.dig(2, 0).area).isEqualTo(Area("""
            ■--
            ■■■
            ■■■
        """))

        assertThat(Area("""
            ---
            ---
            #--
        """).dig(1, 0).area).isEqualTo(Area("""
            ---
            #--
            ■■■
        """))
    }

    @Test
    fun `can dig a debris`() {
        val debris = Debris("""
            ---
            ■#-
            ■■■
        """)

        debris.dig(2)
        debris.dig(2)

        assertThat(debris).isEqualTo(Debris("""
            ■#-
            ■■■
            ■■■
        """))
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