package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.*

class Field(
    val x: Int,
    val y: Int,
    val filling: Filling,
) {

    companion object {
        fun filled(x: Int, y: Int): Field {
            return Field(x, y, FILLED)
        }

        fun bomb(x: Int, y: Int): Field {
            return Field(x, y, BOMB)
        }

        fun soil(x: Int, y: Int): Field {
            return Field(x, y, SOIL)
        }

        fun empty(x: Int, y: Int): Field {
            return Field(x, y, EMPTY)
        }
    }

    constructor(x: Int, y: Int) : this(x, y, EMPTY)
    constructor(x: Int, y: Int, filling: Char) : this(x, y, Filling.of(filling))

    fun up(by: Int): Field = Field(x, y - by, filling)
    fun down(): Field = Field(x, y + 1, filling)
    fun down(by: Int): Field = Field(x, y + by, filling)
    fun left(): Field = left(1)
    fun left(by: Int): Field = Field(x - by, y, filling)
    fun right(): Field = right(1)
    fun right(by: Int): Field = Field(x + by, y, filling)

    fun rotate(width: Int): Field = Field(width - y - 1, x, filling)
    fun plus(field: Field): Field = Field(x + field.x, y + field.y, filling)
    fun minus(field: Field): Field = Field(x - field.x, y - field.y, filling)
    fun isFilled(): Boolean = filling.isFilled()
    fun collides(): Boolean = filling.collides()
    fun isSoil() = filling == SOIL
    fun falls(): Boolean = filling.isFilled()
    fun special(field: Field, area: Area): Area = filling.special(field, area)

    fun within(area: Area): Boolean = x >= 0 && x < area.width() && y >= 0 && y < area.height()
    fun erase(): Field = empty(x, y)

    override fun toString(): String = "($x,$y,$filling)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Field

        if (x != other.x) return false
        if (y != other.y) return false
        if (filling != other.filling) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x + 100
        result = 31 * result + y + 100
        result = 31 * result + filling.hashCode()
        return result
    }
}