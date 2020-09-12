package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class `A Color` {

    @Test
    @Disabled
    fun `should make a range`() {
        val range = listOf(Color(0, 0, 0), Color(255, 255, 255))
        assertThat(Color.byDepth(range, 0)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.byDepth(range, 50)).isEqualTo(Color(122, 122, 122))
        assertThat(Color.byDepth(range, 100)).isEqualTo(Color(255, 255, 255))
    }

    @Test
    fun `should calc gradient`() {
        val from = Color(0, 0, 0)
        val to = Color(255, 255, 255)

        assertThat(Color.gradient(from, to, 0)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.gradient(from, to, 100)).isEqualTo(Color(255, 255,255))
        assertThat(Color.gradient(from, to, 50)).isEqualTo(Color(127.5f, 127.5f,127.5f))
    }
}