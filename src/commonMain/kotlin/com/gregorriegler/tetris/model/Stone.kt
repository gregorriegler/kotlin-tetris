package com.gregorriegler.tetris.model

class Stone(
    structure: Structure,
    private val frame: TetrisFrame,
) {
    constructor(structure: String, frame: TetrisFrame) : this(Structure(structure), frame)

    var grid: Grid = structure.startingPosition(Grid(frame))
        private set

    fun down() {
        grid = frame.down(grid)
    }

    fun left(debris: Collidable) {
        grid = frame.left(grid, debris)
    }

    fun right(debris: Collidable) {
        grid = frame.right(grid, debris)
    }

    fun rotateWithin(debris: Collidable) {
        grid = rotate(debris, grid)
    }

    private fun rotate(debris: Collidable, toRotate: Grid): Grid {
        val rotated = toRotate.rotate()

        if (!outOfGame(rotated, debris)) {
            return rotated
        }

        for (it in (1..rotated.widthFalling() / 2)) {
            if (!outOfGame(rotated.leftBy(it), debris)) {
                return rotated.leftBy(it)
            }
            if (!outOfGame(rotated.rightBy(it), debris)) {
                return rotated.rightBy(it)
            }
        }
        return toRotate;
    }

    fun landed(obstacle: Collidable) = atBottom() || grid.down().collidesWith(obstacle)

    private fun outOfGame(grid: Grid, debris: Collidable): Boolean {
        return grid.collidesWith(debris) || frame.isOutside(grid)
    }

    private fun atBottom(): Boolean = frame.isAtBottom(grid)

    override fun toString(): String {
        return grid.toString()
    }
}