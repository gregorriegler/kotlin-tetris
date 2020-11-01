package com.gregorriegler.tetris.model

import kotlin.math.roundToInt

class TetrisFrame(
    override val width: Int,
    override val height: Int,
) : Frame {

    fun topCenterFilled(): Field = Field.filled(center(), 0)

    fun left(grid: Grid, debris: Collidable): Grid =
        if (!isAtLeftBorder(grid) && !grid.left().collidesWith(debris))
            grid.left()
        else
            grid

    fun right(grid: Grid, debris: Collidable): Grid =
        if (!isAtRightBorder(grid) && !grid.right().collidesWith(debris))
            grid.right()
        else
            grid

    fun down(grid: Grid): Grid =
        if (!isAtBottom(grid))
            grid.down()
        else
            grid

    private fun isAtLeftBorder(grid: Grid) = grid.leftSideFalling() <= 0
    private fun isAtRightBorder(grid: Grid) = grid.rightSideFalling() >= width - 1
    fun isAtBottom(grid: Grid) = grid.bottomFalling() > height - 2

    private fun center() = width.toDouble().div(2).roundToInt() - 1
    fun isOutside(grid: Grid): Boolean =
        grid.rightSideFalling() > width - 1
                || grid.leftSideFalling() < 0
                || grid.bottomFalling() > height - 1

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