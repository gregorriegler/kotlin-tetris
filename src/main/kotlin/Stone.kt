import java.util.Collections.unmodifiableList

class Stone(
    private val structure: Structure,
    private val frame: Frame
) {
    constructor(frame: Frame) : this(Structure(Field(0,0)), frame)

    private var field:Field = frame.startingField()
    private var area:Area = frame.startingArea(structure)

    fun down() {
        field = frame.below(field)
        area = frame.down(area)
    }

    fun left() {
        field = frame.leftOf(field)
    }

    fun right() {
        field = frame.rightOf(field)
    }

    fun isAt(field: Field): Boolean {
        return field == this.field
    }

    fun isAtArea(field: Field): Boolean {
        return area.covers(field)
    }

    fun state(): List<List<String>> {
        return frame.empty().mapIndexed { y, row ->
            val mutableRow = row.toMutableList()

            if (y == field.y) {
                mutableRow[field.x] = "#"
            }

            unmodifiableList(mutableRow)
        }
    }

    fun areaState(): List<List<String>> {
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
    private fun atBottom(): Boolean = frame.isAtBottom(field)
    private fun collisionWith(debris: Debris) = debris.hasDebris(field.below())
}