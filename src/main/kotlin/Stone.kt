import java.util.Collections.unmodifiableList

class Stone(
    private val structure: List<Field>,
    private val frame: Frame
) {
    constructor(frame: Frame) : this(listOf(Field(0,0)), frame)

    private var field:Field = frame.startingField()

    fun down() {
        field = frame.below(field)
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

    fun state(): List<List<String>> {
        return frame.empty().mapIndexed { y, row ->
            val mutableRow = row.toMutableList()

            if (y == field.y) {
                mutableRow[field.x] = "#"
            }

            unmodifiableList(mutableRow)
        }
    }

    fun landed(debris: Debris) = atBottom() || collisionWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(field)
    private fun collisionWith(debris: Debris) = debris.hasDebris(field.below())
}