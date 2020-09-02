package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.*

class Field(
    val position: Position,
    val filling: Filling,
) : Comparable<Field> {

    val x: Int get() = position.x
    val y: Int get() = position.y

    companion object {
        fun filled(x: Int, y: Int): Field {
            return Field(Position(x, y), FILLED)
        }

        fun bomb(x: Int, y: Int): Field {
            return Field(Position(x, y), BOMB)
        }

        fun soil(x: Int, y: Int): Field {
            return Field(Position(x, y), SOIL)
        }

        fun empty(x: Int, y: Int): Field {
            return Field(Position(x, y), EMPTY)
        }
    }

    constructor(x: Int, y: Int) : this(Position(x, y), EMPTY)
    constructor(x: Int, y: Int, filling: Filling) : this(Position(x, y), filling)
    constructor(position: Position) : this(position, EMPTY)
    constructor(position: Position, filling: Char) : this(position, Filling.of(filling))

    fun up(by: Int): Field = Field(this.position.up(by), filling)
    fun down(): Field = Field(this.position.down(), filling)
    fun down(by: Int): Field = Field(this.position.down(by), filling)
    fun left(): Field = left(1)
    fun left(by: Int): Field = Field(this.position.left(by), filling)
    fun right(): Field = right(1)
    fun right(by: Int): Field = Field(this.position.right(by), filling)
    fun rotate(width: Int): Field = Field(this.position.rotate(width), filling)
    fun plus(position: Position): Field = Field(this.position.plus(position), filling)
    fun minus(position: Position): Field = Field(this.position.minus(position), filling)
    fun isFilled(): Boolean = filling.isFilled()
    fun collides(): Boolean = filling.collides()
    fun isSoil() = filling == SOIL
    fun falls(): Boolean = filling.isFilled()
    fun special(area: Area): Area = filling.special(this.position, area)
    fun collidesWith(area: Area): Boolean = collidesWith(area.fields)
    fun collidesWith(fields: List<Field>): Boolean =
        collides() && fields.any { it.collides() && it.position == position }

    fun within(frame: Frame): Boolean = this.position.within(frame)

    fun erase(): Field = empty(position.x, position.y)

    override fun toString(): String = "(${this.position},$filling)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Field

        if (position != other.position) return false
        if (filling != other.filling) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + filling.hashCode()
        return result
    }

    override fun compareTo(other: Field): Int = position.compareTo(other.position)


}