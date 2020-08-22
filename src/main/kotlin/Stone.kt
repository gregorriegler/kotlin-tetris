class Stone(
    structure: Structure,
    private val frame: Frame,
) {
    constructor(frame: Frame) : this(Structure("#"), frame)

    var area: Area = structure.aboveCentered(Area(frame))
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

    // todo: rotation goes to the right everytime
    fun rotate() {
        area = area.rotate()
    }

    fun has(field: Field): Boolean {
        return area.has(field)
    }

    fun state(): List<List<Filling>> =
        (0 until frame.height).map { y ->
            (0 until frame.width).map { x ->
                area.fillingOf(x, y)
            }
        }.toList()

    fun landed(debris: Debris) = atBottom() || collisionWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    private fun collisionWith(debris: Debris) = debris.collidesWith(area.down())
}