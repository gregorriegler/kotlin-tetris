package com.gregorriegler.tetris.model

class VerticalStack(
    private val fields: List<Field>) {

    companion object {
        fun of(grid: Grid, it: Field): VerticalStack {
            return VerticalStack(VerticalStackIterator(grid, it).asSequence().toList())
        }
    }
    fun toList(): List<Field> {
        return fields
    }

    fun emptyBelow() = Field.empty(fields[0].x, fields[0].y + 1)

    fun height() = fields.size

    fun contains(
        it: Field
    ) = fields.contains(it)

    internal class VerticalStackIterator(val grid: Grid, start: Field) : Iterator<Field> {
        var current: Field = start
        override fun hasNext() = current.falls()
        override fun next(): Field {
            val next = current
            current = grid.above(current.position)
            return next
        }
    }
}