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

    fun left(debris: Debris) {
        grid = frame.left(grid, debris)
    }

    fun right(debris: Debris) {
        grid = frame.right(grid, debris)
    }

    fun rotate(debris: Debris) {
        val rotate = grid.rotate()

        if (!outOfGame(rotate, debris)) {
            grid = rotate
            return
        }

        for(it in (1..rotate.widthFalling() / 2)) {
            if (!outOfGame(rotate.leftBy(it), debris)) {
                grid = rotate.leftBy(it)
                break
            } else if (!outOfGame(rotate.rightBy(it), debris)) {
                grid = rotate.rightBy(it)
                break
            }
        }
    }

    fun landed(debris: Debris) = atBottom() || grid.down().collidesWith(debris.grid)

    private fun outOfGame(grid: Grid, debris: Debris): Boolean {
        return grid.collidesWith(debris.grid) || frame.isOutside(grid)
    }

    private fun atBottom(): Boolean = frame.isAtBottom(grid)

    override fun toString(): String {
        return grid.toString()
    }
}