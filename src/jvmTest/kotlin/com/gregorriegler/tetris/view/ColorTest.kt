package com.gregorriegler.tetris.view

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TetrisColor {
    @Test
    fun `converts to css`() {
        assertThat(
            Color(0f,0f,0f,1f).asCss()
        ).isEqualTo("rgba(0, 0, 0, 1.0)")
    }

    @Test
    fun `can enlighten`() {
        assertThat(
            Color(0f,0f,0f,1f).enlightenBy(10).asCss()
        ).isEqualTo("rgba(10, 10, 10, 1.0)")
    }

    @Test
    fun `can darken`() {
        assertThat(
            Color(255f,255f,255f,1f).darkenBy(55).asCss()
        ).isEqualTo("rgba(200, 200, 200, 1.0)")
    }
}