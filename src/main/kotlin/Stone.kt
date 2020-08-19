import java.util.Collections.unmodifiableList

class Stone(
    private val frame: Frame
) {
    var field:Field = frame.startingField()

    fun down() {
        field = field.below()
    }

    fun left() {
        field = frame.leftOf(field)
    }

    fun right() {
        field = frame.rightOf(field)
    }

    fun landed(debris: Debris) = atBottom() || collisionWith(debris)

    private fun atBottom(): Boolean = frame.isAtBottom(field)

    private fun collisionWith(debris: Debris) = debris.hasDebris(field.below())

    fun render(): List<List<String>> {
        return frame.empty().mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (rowIndex == field.y) {
                mutableRow[field.x] = "#"
            }

            unmodifiableList(mutableRow)
        }
    }
}