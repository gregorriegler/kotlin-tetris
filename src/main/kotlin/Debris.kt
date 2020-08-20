import java.util.Collections.unmodifiableList

class Debris(
    private val frame: Frame,
    private var debris: List<List<String>>,
) {
    constructor(frame: Frame) : this(frame, frame.empty())
    constructor(debris: String) : this(
        Frame(
            debris.trimIndent().substringBefore('\n').length,
            debris.trimIndent().count { it == '\n' } + 1
        ),
        debris.trimIndent()
            .split("\n")
            .map { row -> row.chunked(1) }
            .toList()
    )

    fun width(): Int {
        return frame.width
    }

    fun height(): Int {
        return frame.height
    }

    fun add(stone: Stone) {
        debris = debris.mapIndexed { y, row ->
            val mutableRow = row.toMutableList()
            (0 until frame.width).forEach { x ->
                if (stone.isAt(Field(x, y))) mutableRow[x] = Field.STONE
            }
            unmodifiableList(mutableRow)
        }
    }

    fun isAt(area: Area): Boolean =
        area.fields
            .filter { field -> field.y >= 0 }
            .any { isAt(it) }

    fun isAt(field: Field): Boolean = debris[field.y][field.x] != Field.EMPTY

    fun dissolveFilledRows(): Int {
        val filledRows = filledRows()
        debris = makeEmptyRowsFor(filledRows) + removeRows(filledRows)
        return filledRows.size
    }

    private fun removeRows(rows: List<Int>) =
        debris.filterIndexed { index, _ -> !rows.contains(index) }.toList()

    private fun makeEmptyRowsFor(filledLines: List<Int>): List<List<String>> {
        return filledLines.indices
            .map { emptyRow() }
            .toList()
    }

    private fun filledRows(): List<Int> =
        debris.withIndex()
            .filter { it -> it.value.all { isFilled(it) } }
            .map { it.index }
            .toList()

    private fun emptyRow(): List<String> {
        return (0 until frame.width)
            .map { Field.EMPTY }
            .toList()
    }

    fun stateWithStone(stone: Stone): List<List<String>> {
        val stoneState = stone.state()
        return debris.mapIndexed { y, row ->
            row.mapIndexed { x, column ->
                if (isFilled(column) || isFilled(stoneState[y][x]))
                    Field.STONE
                else
                    Field.EMPTY
            }
        }
    }

    private fun isFilled(field: String) = field != Field.EMPTY

    override fun toString(): String {
        return "\n" + Tetris.draw(debris) + "\n"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Debris

        if (frame != other.frame) return false
        if (debris != other.debris) return false

        return true
    }

    override fun hashCode(): Int {
        var result = frame.hashCode()
        result = 31 * result + debris.hashCode()
        return result
    }
}