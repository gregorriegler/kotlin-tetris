package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.*
import com.gregorriegler.tetris.view.Color

class Field(
    val position: MovablePosition,
    val filling: Filling,
) : Comparable<Field>, Position {

    companion object {
        fun filled(position: MovablePosition): Field {
            return Field(position, FILLED)
        }

        fun bomb(position: MovablePosition): Field {
            return Field(position, BOMB)
        }

        fun soil(position: MovablePosition): Field {
            return Field(position, SOIL)
        }

        fun empty(position: MovablePosition): Field {
            return Field(position, EMPTY)
        }
    }

    constructor(x: Int, y: Int) : this(MovablePosition(x, y), EMPTY)
    constructor(x: Int, y: Int, filling: Filling) : this(MovablePosition(x, y), filling)
    constructor(position: MovablePosition) : this(position, EMPTY)
    constructor(position: MovablePosition, filling: Char) : this(position, Filling.of(filling))

    override val x: Int get() = position.x
    override val y: Int get() = position.y
    fun color(depth: Int, palette: List<Color>) : Color = filling.color(position.y + depth, palette)

    fun upBy(amount: Int): Field = Field(this.position.upBy(amount), filling)
    fun down(): Field = downBy(1)
    fun downBy(amount: Int): Field = Field(this.position.downBy(amount), filling)
    fun left(): Field = leftBy(1)
    fun leftBy(amount: Int): Field = Field(this.position.leftBy(amount), filling)
    fun right(): Field = rightBy(1)
    fun rightBy(amount: Int): Field = Field(this.position.right(amount), filling)
    fun plus(position: MovablePosition): Field = Field(this.position.plus(position), filling)
    fun minus(position: MovablePosition): Field = Field(this.position.minus(position), filling)
    fun rotate(width: Int): Field = Field(this.position.rotate(width), filling)
    fun isFilled(): Boolean = filling.isFilled()
    fun isEmpty(): Boolean = filling.isEmpty()
    fun collides(): Boolean = filling.collides()
    fun isSoil() = filling == SOIL
    fun falls(): Boolean = filling.isFilled()
    fun special(area: Area): Area = filling.special(this.position, area)
    fun collidesWith(area: Area): Boolean = collidesWith(area.fields)
    fun collidesWith(fields: List<Field>): Boolean =
        collides() && fields.any { it.collides() && it.position == position }

    fun within(frame: Frame): Boolean = this.position.within(frame)

    fun erase(): Field = empty(MovablePosition(position.x, position.y))

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