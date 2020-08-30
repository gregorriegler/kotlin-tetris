package com.gregorriegler.tetris.model

fun Area.erase(area: Area): Area = erase(area.fields)

fun Area.erase(fields: List<Field>): Area = Area(this.fields.map {
    if (it.collidesWith(fields) || (it.isSoil() && above(it).collidesWith(fields))) {
        it.erase()
    } else {
        it
    }
})

fun Area.eraseFilledRows(): Pair<Area, Int> {
    val filledRows = filledRows()
    val remainingArea = erase(filledRows)
    return Pair(remainingArea, filledRows.size)
}

fun Area.filledRows(): List<Field> {
    return (top()..bottom())
            .filter { y -> row(y).all { it.isFilled() || it.isSoil() } }
            .filterNot { y -> row(y).all { it.isSoil() } }
            .flatMap { y ->
                row(y).filterNot { it.isSoil() }
                    .map { field -> Field(field.x, field.y, field.filling) }
            }

}