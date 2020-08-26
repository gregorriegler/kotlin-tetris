package com.gregorriegler.tetris.model

class Debris(
    private var debris: Area,
) {
    constructor(frame: Frame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    fun add(stone: Stone) {
        debris = debris.combine(stone.area).within(debris)
    }

    fun erase(area: Area): Int {
        debris = debris.erase(area)
        return area.size()
    }

    fun eraseFilledRows(): Int {
        val removed = debris.eraseFilledRowsNew()
        debris = removed.first
        return removed.second
    }

    fun fall() {
        debris = debris.fall()
    }

    fun width(): Int = debris.width()
    fun height(): Int = debris.height()
    fun collidesWith(area: Area): Boolean = debris.collidesWith(area)
    fun collidesWith(field: Field): Boolean = debris.collidesWith(field)

    fun withStone(stone: Stone): Area =
        debris.combine(stone.area)
            .within(debris)

    fun specials() {
        debris = debris.specials()
    }

    fun dig(amount: Int) {
        debris = debris.dig(amount)
    }

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