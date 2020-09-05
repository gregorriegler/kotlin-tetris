package com.gregorriegler.tetris.model

class Position(
    val x: Int,
    val y: Int
) : Comparable<Position> {
    fun upBy(amount: Int): Position = Position(this.x, this.y - amount)
    fun down(): Position = Position(this.x, this.y + 1)
    fun downBy(amount: Int): Position = Position(this.x, this.y + amount)
    fun left(): Position = leftBy(1)
    fun leftBy(amount: Int): Position = Position(this.x - amount, this.y)
    fun right(): Position = right(1)
    fun right(by: Int): Position = Position(this.x + by, this.y)
    fun rotate(width: Int): Position = Position(width - this.y - 1, this.x)
    fun plus(position: Position): Position = Position(this.x + position.x, this.y + position.y)
    fun minus(position: Position): Position = Position(this.x - position.x, this.y - position.y)
    fun within(frame: Frame): Boolean = this.x >= 0 && this.x < frame.width && this.y >= 0 && this.y < frame.height

    override fun compareTo(other: Position): Int =
        when {
            this.y != other.y -> {
                this.y - other.y
            }
            this.x != other.x -> {
                this.x - other.x
            }
            else -> 0
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "($x,$y)"
    }
}