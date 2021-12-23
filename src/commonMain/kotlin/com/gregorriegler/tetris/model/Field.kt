package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.*

class Field(
    val position: Position,
    val filling: Filling,
) : Position, Collidable {

    companion object {
        fun filled(x: Int, y: Int): Field = Field(x, y, FALLING)
        fun bomb(x: Int, y: Int): Field = Field(x, y, BOMB)
        fun soil(x: Int, y: Int): Field = Field(x, y, SOIL)
        fun coin(x: Int, y: Int): Field = Field(x, y, COIN)
        fun empty(position: Position): Field = Field(position, EMPTY)
        fun empty(x: Int, y: Int): Field = Field(x, y, EMPTY)
        fun soilOrCoin(x: Int, y: Int, coinPercentage: Int): Field = Field(x, y, Filling.soilOrCoin(coinPercentage))
    }

    constructor(x: Int, y: Int) : this(Position.of(x, y), EMPTY)
    constructor(x: Int, y: Int, filling: Filling) : this(Position.of(x, y), filling)
    constructor(x: Int, y: Int, filling: Char) : this(Position.of(x, y), Filling.of(filling))

    override val x: Int get() = position.x
    override val y: Int get() = position.y
    fun color(palette: Palette, depth: Int): Color = filling.color(palette, y + depth)


    fun fall(): Field = filling.fall(this)
    override fun upBy(amount: Int): Field = Field(this.position.upBy(amount), filling)
    override fun down(): Field = downBy(1)
    override fun downBy(amount: Int): Field = Field(this.position.downBy(amount), filling)
    override fun left(): Field = leftBy(1)
    override fun leftBy(amount: Int): Field = Field(this.position.leftBy(amount), filling)
    override fun right(): Field = rightBy(1)
    override fun rightBy(amount: Int): Field = Field(this.position.rightBy(amount), filling)
    override fun rotate(grid: Grid) = Field(this.position.rotate(grid), filling)


    override fun plus(position: Position): Field = Field(this.position.plus(position), filling)
    override fun minus(position: Position): Field = Field(this.position.minus(position), filling)

    fun isEmpty(): Boolean = filling.isEmpty()
    fun isSoilOrCoin() = filling == SOIL || filling == COIN
    fun falls(): Boolean = filling.falls()
    fun special(grid: Grid): EraseResult = filling.special(this.position, grid)
    fun erase(grid: Grid): FieldScore {
        return if (grid.collidesWith(this)) {
            erase()
        } else {
            FieldScore(this, 0)
        }
    }

    override fun collidingAt(): List<Position> = filling.collidesAt(this.position)

    fun erase(): FieldScore = FieldScore(empty(x, y), filling.score())
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
}