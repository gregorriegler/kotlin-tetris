package com.gregorriegler.tetris.model

internal class VerticalStackIterator(val grid: Grid, start: Field) : Iterator<Field> {
    var current: Field = start
    override fun hasNext() = current.falls()
    override fun next(): Field {
        val next = current
        current = grid.above(current.position)
        return next
    }
}