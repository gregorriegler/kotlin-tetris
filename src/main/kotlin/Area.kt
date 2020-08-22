open class Area(val fields: Set<Field>) {

    companion object {
        fun parseFields(string: String): Set<Field> {
            return string.trimIndent()
                .split("\n")
                .flatMapIndexed { y, row ->
                    row.chunked(1)
                        .withIndex()
                        .filterNot { it.value == Filling.INDENT_VALUE }
                        .map { Field(it.index, y, it.value) }
                }.toSet()
        }
    }

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: Frame) : this(
        frame.rows().flatMap { y -> frame.columns().map { x -> Field.empty(x, y) } }.toSet()
    )

    fun down(by: Int): Area = Area(fields.map { it.down(by) }.toSet())
    fun down(): Area = Area(fields.map { it.down() }.toSet())
    fun left(): Area = Area(fields.map { it.left() }.toSet())
    fun right(): Area = Area(fields.map { it.right() }.toSet())

    private fun size() = maxOf(width(), height())
    fun width(): Int = (rightSide() - leftSide()) + 1
    fun height(): Int = if (fields.isEmpty()) 0 else (bottom() - top()) + 1

    private fun leftSide(): Int = fields.map { it.x }.minOrNull() ?: 0
    private fun rightSide(): Int = fields.map { it.x }.maxOrNull() ?: 0
    private fun top(): Int = fields.map { it.y }.minOrNull() ?: 0
    private fun bottom(): Int = fields.map { it.y }.maxOrNull() ?: 0

    fun leftSideOfFilled(): Int = fields.filter { it.isFilled() }.map { it.x }.minOrNull() ?: 0
    fun rightSideOfFilled(): Int = fields.filter { it.isFilled() }.map { it.x }.maxOrNull() ?: 0
    fun bottomOfFilled(): Int = fields.filter { it.isFilled() }.map { it.y }.maxOrNull() ?: 0

    fun state(): List<List<Filling>> =
        (0 until height()).map { y ->
            (0 until width()).map { x ->
                fillingOf(x, y)
            }
        }.toList()

    fun fillingOf(x: Int, y: Int): Filling = fields.find { it.x == x && it.y == y }?.filling ?: Filling.EMPTY

    fun collides(field: Field): Boolean = fields.contains(field)

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(size()) }
            .map { field -> field.plus(distance()) }
            .toSet()
    )

    private fun distance() = Field(leftSide(), top())

    fun combine(area: Area): Area =
        Area(fields.map { it.y }.plus(area.fields.map { it.y })
            .flatMap { y ->
                fields.map { it.x }.plus(area.fields.map { it.x }).map { x ->
                    Field(x, y, fillingOf(x, y).or(area.fillingOf(x, y)))
                }
            }.toSet())

    fun collides(area: Area): Boolean = fields.any { area.collides(it) }

    fun aboveCentered(area: Area): Area = move(Field(
        (area.width() - width()) / 2,
        area.top() - height()
    ))

    private fun move(vector: Field): Area = Area(fields.map { field -> field.plus(vector) }.toSet())

    fun within(area: Area): Area = Area(fields.filter { it.within(area) }.toSet())

    fun dissolveFilledRows(): Pair<Area, Int> {
        val withoutFilledRows = withoutFilledRows()
        val count = height() - withoutFilledRows.height()
        val down = withoutFilledRows.down(count)
        val emptyLinesToAdd = addEmptyLinesOnTop(count)
        return Pair(emptyLinesToAdd.combine(down), count)
    }

    private fun addEmptyLinesOnTop(removedRowsCount: Int): Area {
        return Area(
            (top() until top() + removedRowsCount)
                .flatMap { y -> (0 until width()).map { x -> Field.empty(x, y) } }
                .toSet())
    }

    private fun withoutFilledRows(): Area {
        return Area(
            (top() until bottom() + 1)
                .filter { y -> (0 until width()).map { x -> fillingOf(x, y) }.any { it == Filling.EMPTY } }
                .flatMapIndexed { newY, y -> (0 until width()).map { x -> Field(x, newY, fillingOf(x, y)) } }
                .toSet()
        )
    }

    override fun toString(): String = "\n" + Tetris.draw(state()) + "\n"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int = fields.hashCode()
}