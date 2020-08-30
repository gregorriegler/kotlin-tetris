package com.gregorriegler.tetris.model

class Stone(
    structure: Structure,
    private val frame: Frame,
) {
    constructor(structure: String, frame: Frame) : this(Structure(structure), frame)

    var area: Area = structure.startingPosition(Area(frame))
        private set

    fun down() {
        area = frame.down(area)
    }

    fun left(debris: Debris) {
        area = frame.left(area, debris)
    }

    fun right(debris: Debris) {
        area = frame.right(area, debris)
    }

    fun rotate(debris: Debris) {
        val rotate = area.rotate()

        if (!outOfGame(rotate, debris)) {
            area = rotate
            return
        }

        for(it in (1..rotate.widthNonEmpty() / 2)) {
            if (!outOfGame(rotate.left(it), debris)) {
                area = rotate.left(it)
                break
            } else if (!outOfGame(rotate.right(it), debris)) {
                area = rotate.right(it)
                break
            }
        }
    }

    fun outOfGame(area: Area, debris: Debris): Boolean {
        return area.collidesWith(debris.area) || frame.isOutside(area)
    }

    fun landed(debris: Debris) = atBottom() || area.down().collidesWith(debris.area)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    override fun toString(): String {
        return area.toString()
    }
}