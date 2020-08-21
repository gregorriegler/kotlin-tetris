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

    private fun size() = maxOf(width(), height())
    fun width(): Int = (rightSide() - leftSide()) + 1
    fun height(): Int = (bottom() - top()) + 1

    private fun leftSide(): Int = fields.map { it.x }.minOrNull()!!
    private fun rightSide(): Int = fields.map { it.x }.maxOrNull()!!
    private fun top(): Int = fields.map { it.y }.minOrNull()!!
    private fun bottom(): Int = fields.map { it.y }.maxOrNull()!!

    fun leftSideOfFilled(): Int = fields.filter { it.isFilled() }.map { it.x }.minOrNull()!!
    fun rightSideOfFilled(): Int = fields.filter { it.isFilled() }.map { it.x }.maxOrNull()!!
    fun bottomOfFilled(): Int = fields.filter { it.isFilled() }.map { it.y }.maxOrNull()!!

    private fun state(): List<List<Filling>> =
        (0 until height()).map { y ->
            (0 until width()).map { x ->
                fillingOf(x, y)
            }
        }.toList()

    fun fillingOf(x: Int, y: Int): Filling = fields.find { it.x == x && it.y == y }?.filling ?: Filling.EMPTY

    fun has(field: Field): Boolean = fields.contains(field)

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(size()) }
            .map { field -> field.plus(distance()) }
            .toSet()
    )

    private fun distance() = Field(leftSide(), top())

    override fun toString(): String = "\n" + Tetris.draw(state()) + "\n"

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