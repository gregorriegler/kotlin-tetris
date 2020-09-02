package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.*

class Field (
    position: Position,
    val filling: Filling,
) : Position, Comparable<Field> {

    override val x: Int = position.x
    override val y: Int = position.y

    companion object {
        fun filled(x: Int, y: Int): Field {
            val position = SimplePosition(x, y)
            return Field(position, FILLED)
        }

        fun bomb(x: Int, y: Int): Field {
            val position = SimplePosition(x, y)
            return Field(position, BOMB)
        }

        fun soil(x: Int, y: Int): Field {
            val position = SimplePosition(x, y)
            return Field(position, SOIL)
        }

        fun empty(x: Int, y: Int): Field {
            val position = SimplePosition(x, y)
            return Field(position, EMPTY)
        }
    }

    constructor(x: Int, y: Int) : this(SimplePosition(x, y), EMPTY)
    constructor(x: Int, y: Int, filling: Filling) : this(SimplePosition(x, y), filling)
    constructor(position: Position) : this(position, EMPTY)
    constructor(position: Position, filling: Char) : this(position, Filling.of(filling))

    fun up(by: Int): Field = Field(SimplePosition(this.x, this.y - by), filling)
    fun down(): Field = Field(SimplePosition(this.x, this.y + 1), filling)
    fun down(by: Int): Field = Field(SimplePosition(this.x, this.y + by), filling)
    fun left(): Field = left(1)
    fun left(by: Int): Field = Field(SimplePosition(this.x - by, this.y), filling)
    fun right(): Field = right(1)
    fun right(by: Int): Field = Field(SimplePosition(this.x + by, this.y), filling)

    fun rotate(width: Int): Field = Field(SimplePosition(width - this.y - 1, this.x), filling)
    fun plus(position: Position): Field = Field(SimplePosition(this.x + position.x, this.y + position.y), filling)
    fun minus(position: Position): Field = Field(SimplePosition(this.x - position.x, this.y - position.y), filling)
    fun isFilled(): Boolean = filling.isFilled()
    fun collides(): Boolean = filling.collides()
    fun isSoil() = filling == SOIL
    fun falls(): Boolean = filling.isFilled()
    fun special(area: Area): Area = filling.special(this, area)

    fun collidesWith(area: Area): Boolean =  collidesWith(area.fields)
    fun collidesWith(fields: List<Field>): Boolean = collides() && fields.any {it.collides() && it.x == this.x && it.y == this.y }

    fun within(frame: Frame): Boolean = this.x >= 0 && this.x < frame.width && this.y >= 0 && this.y < frame.height

    fun erase(): Field = empty(this.x, this.y)

    override fun toString(): String = "(${this.x},${this.y},$filling)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Field

        if (this.x != other.x) return false
        if (this.y != other.y) return false
        if (filling != other.filling) return false

        return true
    }

    override fun hashCode(): Int {
        var result = this.x + 100
        result = 31 * result + this.y + 100
        result = 31 * result + filling.hashCode()
        return result
    }

    override fun compareTo(other: Field): Int {
        return if(this.y != other.y) {
            this.y - other.y
        } else if(this.x != other.x) {
            this.x - other.x
        } else 0
    }
}