import java.util.*

class Stone(
    private val frame: Frame
) {
    var x: Int = frame.center()
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

    fun render(): List<List<String>> {
        return frame.drawEmpty().mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (rowIndex == y) {
                mutableRow[x] = "#"
            }

            Collections.unmodifiableList(mutableRow)
        }
    }
}