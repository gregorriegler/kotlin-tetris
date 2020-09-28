package com.gregorriegler.tetris.model

class Eraser (val grid: Grid) {

    fun erase(gridAsString: String): EraseResult = erase(Grid(gridAsString))
    fun erase(grid: Grid): EraseResult = erase(grid.fields)
    fun eraseFilledRows(): EraseResult = erase(grid.filledRowsAndSoilBelow())
    fun specials(): EraseResult = grid.fields.fold(
        EraseResult(grid, 0)
    ) { previous, field -> field.special(previous.grid).plus(previous.score) }

    private fun erase(fields: List<Field>): EraseResult {
        val fieldScores = grid.fields.map { it.erase(fields) }
        return EraseResult(Grid(fieldScores.map { it.field }), fieldScores.sumOf { it.score })
    }

}
