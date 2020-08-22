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

    fun rotate() {
        area = area.rotate()
    }

    fun has(field: Field): Boolean {
        return area.collides(field)
    }

    fun state(): List<List<Filling>> =
        frame.rows().map { y ->
            frame.columns().map { x ->
                area.fillingOf(x, y)
            }
        }.toList()

    fun landed(debris: Debris) = atBottom() || collidesWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    private fun collidesWith(debris: Debris) = debris.collidesWith(area.down())
}