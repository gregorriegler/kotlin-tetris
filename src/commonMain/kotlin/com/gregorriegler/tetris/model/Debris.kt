package com.gregorriegler.tetris.model

class Debris(
    var area: Area,
    var depth: Int = 0
) {
    constructor(frame: TetrisFrame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    val fields: List<Field> get() = area.fields

    fun add(stone: Stone) {
        area = area.combine(stone.area).within(area)
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

    fun withStone(stone: Stone): Debris =
        Debris(
            area.combine(stone.area)
                .within(area),
            depth
        )

    fun specials() {
        area = area.specials()
    }

    fun dig(amount: Int) {
        val dig = area.dig(amount)
        area = dig.first
        depth += dig.second
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