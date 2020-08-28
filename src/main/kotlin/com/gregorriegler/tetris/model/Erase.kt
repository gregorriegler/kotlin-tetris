package com.gregorriegler.tetris.model

fun Area.erase(area: Area): Area = Area(fields.map {
    if (area.collidesWith(it)) {
        it.erase()
    } else {
        it
    }
})

fun Area.eraseFilledRows(): Pair<Area, Int> {
    val filledRows = filledRows()
    val remainingArea = erase(filledRows)
    return Pair(remainingArea, filledRows.sizeNonEmpty())
}

fun Area.filledRows(): Area {
    return Area(
        (top()..bottom())
            .filter { y -> row(y).all { it.isFilled() || it.isSoil() } }
            .filterNot { y -> row(y).all { it.isSoil() } }
            .flatMap { y ->
                row(y).filterNot { it.isSoil() }
                    .map { field -> Field(field.x, field.y, field.filling) }
            }
    )
}