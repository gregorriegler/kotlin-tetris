import java.util.*

class Debris(
    private val frame: Frame,
) {
    private var debris: List<List<String>> = frame.empty()

    fun add(stone: Stone) {
        debris = debris.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()
            if (stone.field.y == rowIndex) {
                mutableRow[stone.field.x] = "#"
            }
            Collections.unmodifiableList(mutableRow)
        }
    }

    fun hasDebris(field: Field): Boolean {
        return debris[field.y][field.x] != Field.EMPTY
    }

    fun bottomLineFilled(): Boolean {
        return debris.last().all { isStone(it) }
    }

    fun dissolveLine() {
        debris = listOf((0 until frame.width)
            .map { Field.EMPTY }.toList()) + debris.dropLast(1)
            .toMutableList()
    }

    fun drawWithStone(stone: Stone): List<List<String>> {
        return debris.mapIndexed { rowIndex, landedRow ->
            landedRow.mapIndexed { columnIndex, column ->
                if (isStone(column) || isStone(stone.render()[rowIndex][columnIndex]))
                    "#"
                else
                    Field.EMPTY
            }
        }
    }

    private fun isStone(field: String) = field != Field.EMPTY
}