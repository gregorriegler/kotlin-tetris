package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class Debris(
    var grid: Grid,
    var depth: Int,
    val palette: List<Color>
) {
    constructor(grid: Grid, depth: Int = 0) : this(
        grid, depth, listOf(
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
        )
    )

    constructor(frame: TetrisFrame) : this(Grid(frame))
    constructor(debris: String) : this(Grid(debris))

    fun asStones(display: Frame): List<TetrisStone> {
        return grid.asStones(display, depth, palette)
    }

    fun add(stone: Stone) {
        grid = grid.combine(stone.grid).within(grid)
    }

    fun eraseFilledRows(): Int {
        val removed = Eraser(grid).eraseFilledRows()
        grid = removed.grid
        return removed.score
    }

    fun fall() {
        grid = grid.fall()
    }

    fun width(): Int = grid.width
    fun height(): Int = grid.height

    fun withStone(stone: Stone): Debris =
        Debris(
            grid.combine(stone.grid)
                .within(grid),
            depth,
            palette
        )

    fun specials(): Int {
        val specialsResult = Eraser(grid).specials()
        grid = specialsResult.grid
        return specialsResult.score
    }

    fun dig(amount: Int) {
        val dig = grid.dig(amount, coinPercentage(depth))
        grid = dig.grid
        depth += dig.depth
    }

    fun collidesWith(field: Field): Boolean = grid.collidesWith(field)

    private fun coinPercentage(depth: Int) = depth/100 + 2

    override fun toString(): String = grid.toString()

    override fun hashCode(): Int = grid.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Debris

        if (grid != other.grid) return false

        return true
    }
}