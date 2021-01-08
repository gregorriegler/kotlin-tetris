package com.gregorriegler.tetris.model

interface Position : Comparable<Position> {
    val x: Int
    val y: Int
    companion object {
        fun of(x: Int, y: Int): Position = SimplePosition(x, y)
    }

    fun up(): Position = upBy(1)

    fun upBy(amount: Int): Position = of(this.x, this.y - amount)
    fun down(): Position = downBy(1)
    fun downBy(amount: Int): Position = of(this.x, this.y + amount)
    fun left(): Position = leftBy(1)
    fun leftBy(amount: Int): Position = of(this.x - amount, this.y)
    fun right(): Position = rightBy(1)
    fun rightBy(amount: Int): Position = of(this.x + amount, this.y)
    fun rotate(width: Int): Position = of(width - this.y - 1, this.x)
    fun plus(position: Position): Position = of(this.x + position.x, this.y + position.y)
    fun minus(position: Position): Position = of(this.x - position.x, this.y - position.y)
    fun within(frame: Frame): Boolean = this.x >= 0 && this.x < frame.width && this.y >= 0 && this.y < frame.height
    fun circle(radius: Int): List<Position> =
        (this.y - radius..this.y + radius)
            .flatMap { y ->
                (this.x - radius..this.x + radius).filter { x ->
                    ((x - this.x).squared() + (y - this.y).squared() <= radius.squared())
                }.map { x -> of(x, y) }
            }
    fun Int.squared() = this * this

    override fun compareTo(other: Position): Int =
        when {
            this.y != other.y -> this.y - other.y
            this.x != other.x -> this.x - other.x
            else -> 0
        }
}

