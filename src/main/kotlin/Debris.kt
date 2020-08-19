import java.util.Collections.unmodifiableList

class Debris (
    private val frame: Frame,
    private var debris: List<List<String>>
){
    constructor(frame: Frame) : this(frame, frame.empty())

    fun add(stone: Stone) {
        debris = debris.mapIndexed { y, row ->
            val mutableRow = row.toMutableList()
            (0 until frame.width).forEach { x ->
                if (stone.isAt(Field(x, y))) mutableRow[x] = Field.STONE
            }
            unmodifiableList(mutableRow)
        }
    }

    fun hasDebris(field: Field): Boolean {
        return debris[field.y][field.x] != Field.EMPTY
    }

    fun bottomLineFilled(): Boolean {
        return debris.last().all { isFilled(it) }
    }

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