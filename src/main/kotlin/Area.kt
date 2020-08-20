import java.util.*

open class Area(val fields: Set<Field>) {

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(
        string.trimIndent()
            .split("\n")
            .flatMapIndexed { y, row ->
                row.chunked(1)
                    .withIndex()
                    .filter { field -> field.value != Field.EMPTY }
                    .map { Field(it.index, y) }
            }.toSet()
    )

    fun width(): Int {
        return (rightSide() - leftSide()) + 1
    }

    fun height(): Int {
        val top = fields.map { it.y }.minOrNull()!!
        fields.map { it.y }.maxOrNull()!!
        return (bottom() - top) + 1
    }

    fun bottom(): Int {
        return fields.map { it.y }.maxOrNull()!!
    }

    fun leftSide(): Int {
        return fields.map { it.x }.minOrNull()!!
    }

    fun rightSide(): Int {
        return fields.map { it.x }.maxOrNull()!!
    }

    fun down(): Area = Area(fields.map { field -> field.below() }.toSet())

    fun left(): Area = Area(fields.map { field -> field.toTheLeft() }.toSet())

    fun right(): Area = Area(fields.map { field -> field.toTheRight() }.toSet())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int {
        return fields.hashCode()
    }

    override fun toString(): String {
        return "\n" + Tetris.draw(state()) + "\n"
    }

    fun state(): List<List<String>> {
        return Frame(rightSide() + 1, bottom() +1).empty().mapIndexed { y, row ->
            val mutableRow = row.toMutableList()

            row.mapIndexed { x, _ ->
                if(covers(Field(x, y))) {
                    mutableRow[x] = Field.STONE
                }
            }

            Collections.unmodifiableList(mutableRow)
        }
    }

    fun covers(field: Field): Boolean {
        return fields.contains(field)
    }

    fun rotate(): Area = Area(fields.map { field -> field.rotate(width(), height()) }.toSet())


}