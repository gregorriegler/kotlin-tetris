package com.gregorriegler.tetris.model

class Eraser (val grid: Grid) {

    fun erase(gridAsString: String): EraseResult = erase(Grid(gridAsString))
    fun erase(grid: Grid): EraseResult = erase(grid.fields)
    fun eraseFilledRows(): EraseResult = erase(grid.filledRows())
    fun specials(): EraseResult = grid.fields.fold(
        EraseResult(grid, 0)
    ) { previous, field -> field.special(previous.grid).plus(previous.score) }

    private fun erase(fields: List<Field>): EraseResult {
        val fieldScores = grid.fields.map { field ->
            if (field.collidesWith(fields) || (field.isSoilOrCoin() && grid.above(field).collidesWith(fields))) {
                field.erase()
            } else {
                FieldScore(field, 0)
            }
        }
        return EraseResult(Grid(fieldScores.map { it.field }), fieldScores.sumOf { it.score })
    }
}
