package com.gregorriegler.tetris.model

open class Area(val fields: Set<Field>) {

    companion object {
        fun circle(center: Field, radius: Int): Area {
            return Area(
                (center.y - radius until center.y + radius + 1)
                    .flatMap { y ->
                        (center.x - radius until center.x + radius + 1)
                            .filter { x ->
                                val dx = x - center.x
                                val dy = y - center.y
                                (dx * dx + dy * dy <= radius * radius)
                            }.map { x -> Field.filled(x, y) }
                    }.toSet())
        }

        fun parseFields(string: String): Set<Field> {
            return string.trimIndent()
                .split("\n")
                .flatMapIndexed { y, row ->
                    val pulls = row.filter { it == Filling.PULL_VALUE }.count() * 2
                    row.toCharArray()
                        .withIndex()
                        .filterNot { it.value == Filling.INDENT_VALUE || it.value == Filling.PULL_VALUE }
                        .map { Field(it.index - pulls, y, it.value) }
                }.toSet()
        }

        fun draw(combined: List<List<Filling>>) =
            combined.joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } }
    }

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: Frame) : this(
        frame.rows().flatMap { y -> frame.columns().map { x -> Field.empty(x, y) } }.toSet()
    )

    private val fieldMap: Map<Int, Map<Int, Field>> =
        fields.groupBy { it.y }.mapValues { (_, field) -> field.associateBy { it.x } }

    fun down(by: Int): Area = Area(fields.map { it.down(by) }.toSet())
    fun down(): Area = Area(fields.map { it.down() }.toSet())
    fun left(): Area = left(1)
    fun left(by: Int): Area = Area(fields.map { it.left(by) }.toSet())
    fun right(): Area = right(1)
    fun right(by: Int): Area = Area(fields.map { it.right(by) }.toSet())

    private fun leftSide(): Int = fields.map { it.x }.minOrNull() ?: 0
    private fun rightSide(): Int = fields.map { it.x }.maxOrNull() ?: 0
    private fun top(): Int = fields.map { it.y }.minOrNull() ?: 0
    private fun bottom(): Int = fields.map { it.y }.maxOrNull() ?: 0

    fun width(): Int = (rightSide() - leftSide()) + 1
    fun height(): Int = if (fields.isEmpty()) 0 else (bottom() - top()) + 1
    private fun size() = maxOf(width(), height())
    fun countFilled(): Int = fields.filter { it.isFilled() }.count()

    fun widthNonEmpty(): Int = (rightSideNonEmpty() - leftSideNonEmpty()) + 1
    fun leftSideNonEmpty(): Int = fields.filter { it.isFilled() }.map { it.x }.minOrNull() ?: 0
    fun rightSideNonEmpty(): Int = fields.filter { it.isFilled() }.map { it.x }.maxOrNull() ?: 0

    fun bottomNonEmpty(): Int = fields.filter { it.isFilled() }.map { it.y }.maxOrNull() ?: 0

    fun outsideOf(frame: Frame): Boolean =
        rightSideNonEmpty() > frame.width - 1
                || leftSideNonEmpty() < 0
                || bottomNonEmpty() > frame.height - 1

    fun state(): List<List<Filling>> =
        (0..bottom()).map { y ->
            (0..rightSide()).map { x ->
                fillingOf(x, y)
            }
        }.toList()

    fun fillingOf(x: Int, y: Int): Filling = fieldMap[y]?.get(x)?.filling ?: Filling.EMPTY

    private fun distance() = Field(leftSide(), top())

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(size()) }
            .map { field -> field.plus(distance()) }
            .toSet()
    )

    fun combine(area: Area): Area =
        Area(fields.map { it.y }.plus(area.fields.map { it.y })
            .flatMap { y ->
                fields.map { it.x }.plus(area.fields.map { it.x }).map { x ->
                    Field(x, y, fillingOf(x, y).or(area.fillingOf(x, y)))
                }
            }.toSet())
    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }

    fun collidesWith(field: Field): Boolean = field.isFilled() && fields.contains(field)

    fun aboveCentered(area: Area): Area = move(Field(
        (area.width() - width()) / 2,
        area.top() - height()
    ))

    private fun move(vector: Field): Area = Area(fields.map { field -> field.plus(vector) }.toSet())

    fun within(area: Area): Area = Area(fields.filter { it.within(area) }.toSet())

    fun erase(area: Area): Area = Area(fields.filterNot { area.collidesWith(it) }.toSet())

    fun eraseFilledRows(): Pair<Area, Int> {
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
            (top()..bottom())
                .filter { y -> (0 until width()).map { x -> fillingOf(x, y) }.any { it == Filling.EMPTY } }
                .flatMapIndexed { newY, y -> (0 until width()).map { x -> Field(x, newY, fillingOf(x, y)) } }
                .toSet()
        )
    }

    override fun toString(): String = "\n" + draw(state()) + "\n"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (fields != other.fields) return false

        return true
    }
    override fun hashCode(): Int = fields.hashCode()
}