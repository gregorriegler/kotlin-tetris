import java.util.Collections.unmodifiableList

class Debris(
    private val frame: Frame,
    private var debris: List<List<Filling>>,
    private var debrisNew: Area,
) {
    constructor(frame: Frame) : this(frame, frame.empty(), Area())
    constructor(debris: String) : this(
        Frame(
            debris.trimIndent().substringBefore('\n').length,
            debris.trimIndent().count { it == '\n' } + 1
        ),
        debris.trimIndent()
            .split("\n")
            .map { row -> row.chunked(1).map { Filling.of(it) } }
            .toList(),
        Area(debris)
    )

    fun width(): Int {
        return frame.width
    }

    fun height(): Int {
        return frame.height
    }

    fun add(stone: Stone) {
        addNew(stone)
        debris = debris.mapIndexed { y, row ->
            val mutableRow = row.toMutableList()
            (0 until frame.width).forEach { x ->
                if (stone.has(Field.filled(x, y))) mutableRow[x] = Filling.FILLED
            }
            unmodifiableList(mutableRow)
        }
    }

    fun addNew(stone: Stone) {
        debrisNew = debrisNew.combine(stone.area)
    }

    fun isAt(area: Area): Boolean =
        area.fields
            .filter { field -> field.y >= 0 }
            .filter { it.isFilled() }
            .any { isAt(it) }

    fun isAt(field: Field): Boolean = field.y >= 0 && field.x >= 0
            && field.y < debris.size
            && field.x < debris[field.y].size
            && debris[field.y][field.x] != Filling.EMPTY

    fun dissolveFilledRows(): Int {
//        dissolveFilledRowsNew()
        val filledRows = filledRows()
        debris = makeEmptyRowsFor(filledRows) + removeRows(filledRows)
        return filledRows.size
    }

//    fun dissolveFilledRowsNew(): Int {
//        (0 until height()).flatMap { y ->
//            (0 until width()).map { x ->
////                debrisNew.
//            }
//        }
//    }

    private fun removeRows(rows: List<Int>) =
        debris.filterIndexed { index, _ -> !rows.contains(index) }.toList()

    private fun makeEmptyRowsFor(filledLines: List<Int>): List<List<Filling>> {
        return filledLines.indices
            .map { emptyRow() }
            .toList()
    }

    private fun filledRows(): List<Int> =
        debris.withIndex()
            .filter { it -> it.value.all { isFilled(it) } }
            .map { it.index }
            .toList()

    private fun emptyRow(): List<Filling> {
        return (0 until frame.width)
            .map { Filling.EMPTY }
            .toList()
    }

    fun stateWithStone(stone: Stone): List<List<Filling>> {
        val stoneState = stone.state()
        return debris.mapIndexed { y, row ->
            row.mapIndexed { x, column ->
                if (isFilled(column) || isFilled(stoneState[y][x]))
                    Filling.FILLED
                else
                    Filling.EMPTY
            }
        }
    }

    private fun isFilled(field: Filling) = field != Filling.EMPTY

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