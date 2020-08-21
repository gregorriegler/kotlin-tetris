import java.util.*

open class Area(val fields: Set<Field>) {

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(
        string.trimIndent()
            .split("\n")
            .flatMapIndexed { y, row ->
                row.chunked(1)
                    .withIndex()
                    .filter { field -> field.value != Field.INDENT }
                    .map { field ->
                        if (field.value != Field.EMPTY) {
                            FilledField(field.index, y)
                        } else {
                            EmptyField(field.index, y)
                        }
                    }
            }.toSet()
    )

    fun filledWidth(): Int {
        return (rightSideOfFilled() - leftSideOfFilled()) + 1
    }

    fun filledHeight(): Int {
        return (bottomOfFilled() - topOfFilled()) + 1
    }

    private fun size() = maxOf(filledWidth(), filledHeight())

    fun top(): Int {
        return fields.map { it.y }.minOrNull()!!
    }

    fun topOfFilled(): Int {
        return fields.filter { it is FilledField }
            .map { it.y }
            .minOrNull()!!
    }

    fun bottomOfFilled(): Int {
        return fields.filter { it is FilledField }
            .map { it.y }
            .maxOrNull()!!
    }

    fun leftSide(): Int {
        return fields.map { it.x }.minOrNull()!!
    }

    fun leftSideOfFilled(): Int {
        return fields.filter { it is FilledField }
            .map { it.x }.minOrNull()!!
    }

    fun rightSideOfFilled(): Int {
        return fields.filter { it is FilledField }
            .map { it.x }
            .maxOrNull()!!
    }

    fun rightSide(): Int {
        return fields.map { it.x }
            .maxOrNull()!!
    }

    fun bottom(): Int {
        return fields.map { it.y }
            .maxOrNull()!!
    }

    fun down(): Area = Area(fields.map { field -> field.below() }.toSet())

    fun left(): Area = Area(fields.map { field -> field.toTheLeft() }.toSet())

    fun right(): Area = Area(fields.map { field -> field.toTheRight() }.toSet())

    fun state(): List<List<String>> {
        return Frame(rightSide() + 1, bottom() + 1)
            .empty()
            .mapIndexed { y, row ->
                val mutableRow = row.toMutableList()

                row.mapIndexed { x, _ ->
                    if (covers(Field(x, y))) {
                        mutableRow[x] = Field.STONE
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

}