import java.util.Collections.unmodifiableList

class Stone(
    structure: Structure,
    private val frame: Frame
) {
    constructor(frame: Frame) : this(Structure("#"), frame)

    private var area:Area = frame.startingArea(structure) //todo remove

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

    fun state(): List<List<String>> {
        return frame.empty().mapIndexed { y, row ->
            val mutableRow = row.toMutableList()

            row.mapIndexed { x, _ ->
                if(area.has(Field.filled(x, y))) {
                    mutableRow[x] = Filling.FILLED.toString()
                }
            }

            unmodifiableList(mutableRow)
        }
    }
    fun landed(debris: Debris) = atBottom() || collisionWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    private fun collisionWith(debris: Debris) = debris.isAt(area.down())
}