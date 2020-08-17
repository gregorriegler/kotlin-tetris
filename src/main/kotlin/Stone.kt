import java.util.*

class Stone(
    var x: Int,
    private val frame: Frame
) {
    var y: Int = -1

    fun down() {
        y += 1
    }

    fun left() {
        x -= 1
    }

    fun right() {
        x += 1
    }

    fun render(emptyBoard: List<List<String>>): List<List<String>> {
        return emptyBoard.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (rowIndex == y) {
                mutableRow[x] = "#"
            }

            Collections.unmodifiableList(mutableRow)
        }
    }
}