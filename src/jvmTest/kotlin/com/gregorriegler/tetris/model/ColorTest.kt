package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A Color` {

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

    @Test
    fun `should make a range`() {
        val palette = Palette(Color(0, 0, 0), Color(255, 255, 255))
        assertThat(Color.colorByDepth(palette, 0, 50)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.colorByDepth(palette, 25, 50)).isEqualTo(Color(127.5f, 127.5f, 127.5f))
        assertThat(Color.colorByDepth(palette, 50, 50)).isEqualTo(Color(255, 255, 255))
    }

    @Test
    fun `should make a range of 3 colors`() {
        val palette = Palette(
            Color(0, 0, 0),
            Color(255, 255, 255),
            Color(0, 0, 0)
        )
        assertThat(Color.colorByDepth(palette, 0, 50)).isEqualTo(Color(0, 0, 0))
        assertThat(Color.colorByDepth(palette, 50, 50)).isEqualTo(Color(255, 255, 255))
        assertThat(Color.colorByDepth(palette, 75, 50)).isEqualTo(Color(127.5f, 127.5f, 127.5f))
    }

    @Test
    fun `should make a range of 9 colors`() {
        val palette = Palette(
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
        assertThat(Color.colorByDepth(palette, 1000, 25)).isEqualTo(Color(0, 0, 0))
    }
}