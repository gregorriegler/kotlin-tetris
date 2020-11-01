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

    fun left(obstacle: Grid) {
        grid = frame.left(grid, obstacle)
    }

    fun right(obstacle: Grid) {
        grid = frame.right(grid, obstacle)
    }

    fun rotate(obstacle: Grid) {
        val rotate = grid.rotate()

        if (!outOfGame(rotate, obstacle)) {
            grid = rotate
            return
        }

        for(it in (1..rotate.widthFalling() / 2)) {
            if (!outOfGame(rotate.leftBy(it), obstacle)) {
                grid = rotate.leftBy(it)
                break
            } else if (!outOfGame(rotate.rightBy(it), obstacle)) {
                grid = rotate.rightBy(it)
                break
            }
        }
    }

    fun landed(obstacle: Grid) = atBottom() || grid.down().collidesWith(obstacle)

    private fun outOfGame(grid: Grid, obstacle: Grid): Boolean {
        return grid.collidesWith(obstacle) || frame.isOutside(grid)
    }

    private fun atBottom(): Boolean = frame.isAtBottom(grid)

    override fun toString(): String {
        return grid.toString()
    }
}