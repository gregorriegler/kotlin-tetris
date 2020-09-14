package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class Debris(
    var area: Area,
    var depth: Int,
    val palette: List<Color>
) {
    constructor(area: Area, depth: Int = 0) : this(
        area, depth, listOf(
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
            Color.random(),
        )
    )

    constructor(frame: TetrisFrame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    val fields: List<Field> get() = area.fields

    fun asStones(display: PositionedFrame): List<TetrisStone> {
        return fields
            .filterNot { it.isEmpty() }
            .map { TetrisStone.of(display, area, it, depth, palette) }
    }

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
            depth,
            palette
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