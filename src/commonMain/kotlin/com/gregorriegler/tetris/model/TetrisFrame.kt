package com.gregorriegler.tetris.model

import kotlin.math.roundToInt

class TetrisFrame(
    override val width: Int,
    override val height: Int,
) : Frame {

    fun topCenterFilled(): Field = Field.filled(center(), 0)
    fun rows() = (0 until height)
    fun columns() = (0 until width)

    fun left(area: Area, debris: Debris): Area =
        if (isAtLeftBorder(area) || area.left().collidesWith(debris.area))
            area
        else
            area.left()

    fun right(area: Area, debris: Debris): Area =
        if (isAtRightBorder(area) || area.right().collidesWith(debris.area))
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
    fun isOutside(area: Area): Boolean =
        area.rightSideNonEmpty() > width - 1
                || area.leftSideNonEmpty() < 0
                || area.bottomNonEmpty() > height - 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as TetrisFrame

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