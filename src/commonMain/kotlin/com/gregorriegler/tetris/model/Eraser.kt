package com.gregorriegler.tetris.model

class Eraser (val area: Area) {

    fun erase(areaString: String): EraseResult = erase(Area(areaString))
    fun erase(area: Area): EraseResult = erase(area.fields)
    fun eraseFilledRows(): EraseResult = erase(area.filledRows())
    fun specials(): EraseResult = area.fields.fold(
        EraseResult(area, 0)
    ) { previous, field -> field.special(previous.area).plus(previous.score) }

    private fun erase(fields: List<Field>): EraseResult {
        val fieldScores = area.fields.map { field ->
            if (field.collidesWith(fields) || (field.isSoilOrCoin() && area.above(field).collidesWith(fields))) {
                field.erase()
            } else {
                FieldScore(field, 0)
            }
        }
        return EraseResult(Area(fieldScores.map { it.field }), fieldScores.sumOf { it.score })
    }
}
