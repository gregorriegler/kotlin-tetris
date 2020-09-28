package com.gregorriegler.tetris.model

data class EraseResult(val grid: Grid, val score: Int) {
    constructor(gridAsString: String, score: Int) : this(Grid(gridAsString), score)

    fun plus(score: Int): EraseResult = EraseResult(grid, this.score + score)
}