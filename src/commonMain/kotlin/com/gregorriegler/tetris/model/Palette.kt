package com.gregorriegler.tetris.model

class Palette(private vararg val colors: Color) {

    companion object {
        fun random(size: Int): Palette {
            return Palette(*Array(size) { Color.random() })
        }
    }

    fun colorByDepth(depth: Int, changeColorEvery: Int): Color {
        val fromIndex: Int = depth / changeColorEvery % colors.size
        val toIndex: Int = (fromIndex + 1) % colors.size
        return Color.gradient(
            colors[fromIndex],
            colors[toIndex],
            depth % changeColorEvery * (100 / changeColorEvery)
        )
    }
}