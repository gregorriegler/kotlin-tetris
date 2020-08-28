package com.gregorriegler.tetris.model

import kotlin.math.roundToInt

class Frame(
    val width: Int,
    val height: Int,
) {

    fun topCenterFilled(): Field = Field.filled(center(), 0)
    fun rows() = (0 until height)
    fun columns() = (0 until width)

    fun left(area: Area, debris: Debris): Area =
        if (isAtLeftBorder(area) || debris.collidesWith(area.left()))
            area
        else
            area.left()

    fun right(area: Area, debris: Debris): Area =
        if (isAtRightBorder(area) || debris.collidesWith(area.right()))
            area
        else
            area.right()

    fun down(area: Area): Area =
        if (area.bottomNonEmpty() < height - 1)
            area.down()
        else
            area

    private fun isAtLeftBorder(area: Area) = area.leftSideNonEmpty() <= 0
    private fun isAtRightBorder(area: Area) = area.rightSideNonEmpty() >= width - 1
    fun isAtBottom(area: Area): Boolean = area.bottomNonEmpty() == height - 1
    private fun center() = width.toDouble().div(2).roundToInt() - 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Frame

        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }
    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}