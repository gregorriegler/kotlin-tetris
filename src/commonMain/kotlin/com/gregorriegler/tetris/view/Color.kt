package com.gregorriegler.tetris.view

import kotlin.random.Random

class Color(
    private val red: Float,
    private val green: Float,
    private val blue: Float,
    private val alpha: Float
) {
    constructor(red: Int, green: Int, blue: Int) : this(red.toFloat(), green.toFloat(), blue.toFloat(), 1f)
    constructor(red: Float, green: Float, blue: Float) : this(red, green, blue, 1f)

    companion object {
        val changeColorEvery: Int = 25
        val black: Color = Color(0f, 0f, 0f)
        val orange: Color = Color(240f, 150f, 50f)
        val grey: Color = Color(11f, 11f, 11f)
        val gold: Color = Color(255f, 215f, 0f)

        fun random(): Color {
            return Color(randomColorPart(), randomColorPart(), randomColorPart())
        }

        private fun randomColorPart() = Random.nextInt(10, 240).toFloat()

        fun colorByDepth(depth: Int, colors: List<Color>, changeColorEvery: Int): Color {
            val palette = Palette(colors)
            return palette.colorByDepth(depth, changeColorEvery)
        }

        class Palette(val colors: List<Color>) {
            fun colorByDepth(depth: Int, changeColorEvery: Int): Color {
                val palette = Palette(colors)
                val fromIndex: Int = depth / changeColorEvery % palette.colors.size
                val toIndex: Int = (fromIndex + 1) % palette.colors.size
                return gradient(palette.colors[fromIndex], palette.colors[toIndex], depth % changeColorEvery * (100 / changeColorEvery))
            }

            private fun gradient(from: Color, to: Color, gradient: Int): Color = Color(
                from.red + (to.red - from.red) / 100 * gradient,
                from.green + (to.green - from.green) / 100 * gradient,
                from.blue + (to.blue - from.blue) / 100 * gradient
            )
        }
    }

    fun enlightenBy(absolute: Int): Color {
        return Color(red + absolute, green + absolute, blue + absolute, alpha)
    }

    fun darkenBy(absolute: Int): Color {
        return Color(red - absolute, green - absolute, blue - absolute, alpha)
    }

    fun asCss(): String = "rgba(${red.toInt()}, ${green.toInt()}, ${blue.toInt()}, ${alpha})"

    override fun toString(): String = "rgba(${red}, ${green}, ${blue}, ${alpha})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Color

        if (red != other.red) return false
        if (green != other.green) return false
        if (blue != other.blue) return false
        if (alpha != other.alpha) return false

        return true
    }

    override fun hashCode(): Int {
        var result = red.hashCode()
        result = 31 * result + green.hashCode()
        result = 31 * result + blue.hashCode()
        result = 31 * result + alpha.hashCode()
        return result
    }
}
