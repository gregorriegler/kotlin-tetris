package com.gregorriegler.tetris.view

class Color(
    private val red: Float,
    private val green: Float,
    private val blue: Float,
    private val alpha: Float
) {

    companion object {
        val black: Color = Color(0f, 0f, 0f, 1f)
        val green: Color = Color(240f, 150f, 50f, 1f)
        val orange: Color = Color(34f, 70f, 6f, 1f)
        val grey: Color = Color(11f, 11f, 11f, 1f)
    }

    fun enlightenBy(absolute: Int): Color {
        return Color(red + absolute, green + absolute, blue + absolute, alpha)
    }

    fun darkenBy(absolute: Int): Color {
        return Color(red - absolute, green - absolute, blue - absolute, alpha)
    }

    fun asCss(): String = "rgba(${red.toInt()}, ${green.toInt()}, ${blue.toInt()}, ${alpha})"

    override fun toString(): String = asCss()

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
