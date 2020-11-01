package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A Color` {

    @Test
    fun `should make a range`() {
        val range = listOf(Color(0, 0, 0), Color(255, 255, 255))
        assertThat(Color.colorByDepth(0, range, 50)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.colorByDepth(25, range, 50)).isEqualTo(Color(127.5f, 127.5f,127.5f))
        assertThat(Color.colorByDepth(50, range, 50)).isEqualTo(Color(255, 255, 255))
    }

    @Test
    fun `should make a range of 3 colors`() {
        val range = listOf(
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0)
        )
        assertThat(Color.colorByDepth(0, range, 50)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.colorByDepth(50, range, 50)).isEqualTo(Color(255, 255, 255))
        assertThat(Color.colorByDepth(75, range, 50)).isEqualTo(Color(127.5f, 127.5f,127.5f))
    }

    @Test
    fun `should make a range of 9 colors`() {
        val range = listOf(
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0)
        )
        assertThat(Color.colorByDepth(1000, range, 25)).isEqualTo(Color(0, 0, 0))
    }
}