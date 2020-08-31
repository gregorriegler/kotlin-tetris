package com.gregorriegler.tetris.model

class Debris(
    var area: Area,
) {
    constructor(frame: TetrisFrame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    fun add(stone: Stone) {
        area = area.combine(stone.area).within(area)
    }

    fun erase(area: Area): Int {
        this.area = this.area.erase(area)
        return area.sizeNonEmpty()
    }

    fun eraseFilledRows(): Int {
        val removed = area.eraseFilledRows()
        area = removed.first
        return removed.second
    }

    fun fall() {
        area = area.fall()
    }

    fun width(): Int = area.width
    fun height(): Int = area.height

    fun withStone(stone: Stone): Area =
        area.combine(stone.area)
            .within(area)

    fun specials() {
        area = area.specials()
    }

    fun dig(amount: Int) {
        area = area.dig(amount)
    }

    override fun toString(): String = area.toString()

    override fun hashCode(): Int = area.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Debris

        if (area != other.area) return false

        return true
    }
}