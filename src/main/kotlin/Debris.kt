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

    fun bottomLineFilled(): Boolean = debris.last().all { isFilled(it) }

    fun dissolveLine() {
        debris = listOf((0 until frame.width)
            .map { Field.EMPTY }.toList()) + debris.dropLast(1)
            .toMutableList()
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
}