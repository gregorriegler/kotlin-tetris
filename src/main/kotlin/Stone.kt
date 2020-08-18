import java.util.Collections.unmodifiableList

class Stone(
    private val frame: Frame
) {
    var x: Int = frame.center()
    var y: Int = -1

    fun down() {
        y += 1
    }

    fun left() {
        if (x > 0) {
            x -= 1
        }
    }

    fun right() {
        if (x + 1 <= frame.width - 1) {
            x += 1
        }
    }

    fun landed(debris: List<List<String>>) = atBottom() || collisionWith(debris)

    fun addToDebris(debris: List<List<String>>): List<List<String>> {
        return debris.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()
            if (y == rowIndex) {
                mutableRow[x] = "#"
            }
            unmodifiableList(mutableRow)
        }
    }

    private fun atBottom(): Boolean = frame.isBottom(y)

    private fun collisionWith(debris: List<List<String>>) = debris[y + 1][x] != Field.EMPTY

    fun render(): List<List<String>> {
        return frame.drawEmpty().mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (rowIndex == y) {
                mutableRow[x] = "#"
            }

            unmodifiableList(mutableRow)
        }
    }
}