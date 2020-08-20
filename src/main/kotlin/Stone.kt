import java.util.Collections.unmodifiableList

class Stone(
    structure: Structure,
    private val frame: Frame
) {
    constructor(frame: Frame) : this(Structure(Field(0,0)), frame)

    private var area:Area = frame.startingArea(structure)

    fun down() {
        area = frame.down(area)
    }

    fun left(debris: Debris) {
        area = frame.left(area, debris)
    }

    fun right(debris: Debris) {
        area = frame.right(area, debris)
    }

    fun isAt(field: Field): Boolean {
        return area.covers(field)
    }

    fun state(): List<List<String>> {
        return frame.empty().mapIndexed { y, row ->
            val mutableRow = row.toMutableList()

            row.mapIndexed { x, _ ->
                if(area.covers(Field(x, y))) {
                    mutableRow[x] = Field.STONE
                }
            }

            unmodifiableList(mutableRow)
        }
    }

    fun landed(debris: Debris) = atBottom() || collisionWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    private fun collisionWith(debris: Debris) = debris.isAt(area.down())
}