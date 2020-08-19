import java.util.Collections.unmodifiableList

class Stone(
    private val frame: Frame
) {
    private var field:Field = frame.startingField()

    fun down() {
        field = field.below()
    }

    fun left() {
        field = frame.leftOf(field)
    }

    fun right() {
        field = frame.rightOf(field)
    }

    fun landed(debris: List<List<String>>) = atBottom() || collisionWith(debris)

    private fun atBottom(): Boolean = frame.isAtBottom(field)

    private fun collisionWith(debris: List<List<String>>) = debris[field.y + 1][field.x] != Field.EMPTY

    fun addToDebris(debris: List<List<String>>): List<List<String>> {
        return debris.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()
            if (field.y == rowIndex) {
                mutableRow[field.x] = "#"
            }
            unmodifiableList(mutableRow)
        }
    }

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