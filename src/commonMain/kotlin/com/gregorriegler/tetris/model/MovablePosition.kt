package com.gregorriegler.tetris.model

class MovablePosition(
    override val x: Int,
    override val y: Int
) : Comparable<MovablePosition>, Position {
    fun up(): MovablePosition = upBy(1)
    fun upBy(amount: Int): MovablePosition = MovablePosition(this.x, this.y - amount)
    fun down(): MovablePosition = downBy(1)
    fun downBy(amount: Int): MovablePosition = MovablePosition(this.x, this.y + amount)
    fun left(): MovablePosition = leftBy(1)
    fun leftBy(amount: Int): MovablePosition = MovablePosition(this.x - amount, this.y)
    fun right(): MovablePosition = right(1)
    fun right(by: Int): MovablePosition = MovablePosition(this.x + by, this.y)
    fun rotate(width: Int): MovablePosition = MovablePosition(width - this.y - 1, this.x)
    fun plus(position: MovablePosition): MovablePosition = MovablePosition(this.x + position.x, this.y + position.y)
    fun minus(position: MovablePosition): MovablePosition = MovablePosition(this.x - position.x, this.y - position.y)
    fun within(frame: Frame): Boolean = this.x >= 0 && this.x < frame.width && this.y >= 0 && this.y < frame.height

    override fun compareTo(other: MovablePosition): Int =
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

        other as MovablePosition

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