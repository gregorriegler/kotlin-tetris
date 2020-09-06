package com.gregorriegler.tetris.model

internal class VerticalStackIterator(val area: Area, start: Field) : Iterator<Field> {
    var current: Field = start
    override fun hasNext() = current.isFilled()
    override fun next(): Field {
        val next = current
        current = area.above(current.position)
        return next
    }
}