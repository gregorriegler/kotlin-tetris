import java.util.*

class Stone(
    private val frame: Frame
) {
    var x: Int = frame.center()
    var y: Int = -1

    fun down(): List<List<String>> {
        y += 1
        return render()
    }

    fun left(): List<List<String>> {
        if (x > 0) {
            x -= 1
        }
        return render()
    }

    fun right(): List<List<String>> {
        if (x + 1 <= frame.width - 1) {
            x += 1
        }
        return render()
    }

    fun landed(debris: List<List<String>>) = atBottom() || collisionWith(debris)

    private fun atBottom(): Boolean = frame.isBottom(y)

    private fun collisionWith(debris: List<List<String>>) = debris[y + 1][x] != "_"

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