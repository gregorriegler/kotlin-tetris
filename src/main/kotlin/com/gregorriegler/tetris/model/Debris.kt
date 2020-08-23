package com.gregorriegler.tetris.model

class Debris(
    private var debris: Area,
) {
    constructor(frame: Frame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    fun add(stone: Stone) {
        debris = debris.combine(stone.area).within(debris)
    }

    fun dissolve(area: Area) {
        debris = debris.erase(area)
    }

    fun dissolveFilledRows(): Int {
        val removed = debris.dissolveFilledRows()
        debris = removed.first
        return removed.second
    }
    fun width(): Int = debris.width()

    fun height(): Int = debris.height()
    fun collidesWith(area: Area): Boolean = debris.collides(area)

    fun collidesWith(field: Field): Boolean = debris.collides(field)

    fun withStone(stone: Stone): Area =
        debris.combine(stone.area)
            .within(debris)
    override fun toString(): String = debris.toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Debris

        if (debris != other.debris) return false

        return true
    }
    override fun hashCode(): Int = debris.hashCode()
}