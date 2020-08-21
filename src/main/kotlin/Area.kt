import java.util.*

open class Area(val fields: Set<Field>) {

    constructor(vararg fields: Field) : this(fields.toSet())

    constructor(string: String) : this(
        string.trimIndent()
            .split("\n")
            .flatMapIndexed { y, row ->
                row.chunked(1)
                    .withIndex()
                    .filterNot { it.value == Filling.INDENT_VALUE }
                    .map { Field(it.index, y, it.value) }
            }.toSet()
    )

    fun down(): Area = Area(fields.map { it.down() }.toSet())

    fun left(): Area = Area(fields.map { it.left() }.toSet())

    fun right(): Area = Area(fields.map { it.right() }.toSet())

    private fun size() = maxOf(width(), filledHeight())

    fun width(): Int {
        return (rightSide() - leftSide()) + 1
    }

    fun filledHeight(): Int {
        return (bottomOfFilled() - topOfFilled()) + 1
    }

    private fun top(): Int {
        return fields.map { it.y }.minOrNull()!!
    }

    private fun topOfFilled(): Int {
        return fields.filter { it.isFilled() }
            .map { it.y }
            .minOrNull()!!
    }

    fun bottomOfFilled(): Int {
        return fields.filter { it.isFilled() }
            .map { it.y }
            .maxOrNull()!!
    }

    private fun leftSide(): Int {
        return fields.map { it.x }.minOrNull()!!
    }

    fun leftSideOfFilled(): Int {
        return fields.filter { it.isFilled() }
            .map { it.x }.minOrNull()!!
    }

    fun rightSideOfFilled(): Int {
        return fields.filter { it.isFilled() }
            .map { it.x }
            .maxOrNull()!!
    }

    private fun rightSide(): Int {
        return fields.map { it.x }
            .maxOrNull()!!
    }

    private fun bottom(): Int {
        return fields.map { it.y }
            .maxOrNull()!!
    }

    private fun state(): List<List<String>> {
        return Frame(rightSide() + 1, bottom() + 1)
            .empty()
            .mapIndexed { y, row ->
                val mutableRow = row.toMutableList()

                row.mapIndexed { x, _ ->
                    if (covers(Field(x, y))) {
                        mutableRow[x] = Filling.FILLED.toString()
                    }
                }

                Collections.unmodifiableList(mutableRow)
            }
    }

    fun covers(field: Field): Boolean {
        return fields.contains(field.filled())
    }

    fun rotate(): Area {
        val distance = distance()

        return Area(fields
            .map { field -> field.minus(distance) }
            .map { field -> field.rotate(size()) }
            .map { field -> field.plus(distance) }
            .toSet()
        )
    }

    private fun distance() = Field(leftSide(), top())

    override fun toString(): String {
        return "\n" + Tetris.draw(state()) + "\n"
    }

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

}