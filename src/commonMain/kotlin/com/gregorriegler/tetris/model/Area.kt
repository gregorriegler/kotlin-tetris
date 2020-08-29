package com.gregorriegler.tetris.model

open class Area(val fields: Set<Field>) {

    companion object {
        fun circle(center: Field, radius: Int): Area = Area(
            (center.y - radius..center.y + radius)
                .flatMap { y ->
                    (center.x - radius..center.x + radius)
                        .filter { x ->
                            val dx = x - center.x
                            val dy = y - center.y
                            (dx * dx + dy * dy <= radius * radius)
                        }.map { x -> Field.filled(x, y) }
                })

        fun parseFields(string: String): List<Field> = string.trimIndent()
            .split("\n")
            .flatMapIndexed { y, row ->
                val pulls = row.filter { it == Filling.PULL_VALUE }.count() * 2
                row.toCharArray()
                    .withIndex()
                    .filterNot { it.value == Filling.INDENT_VALUE || it.value == Filling.PULL_VALUE }
                    .map { Field(it.index - pulls, y, it.value) }
            }
    }

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: Frame) : this(frame.rows().flatMap { y -> frame.columns().map { x -> Field.empty(x, y) } })
    constructor(fields: List<Field>) : this(fields.toSet())

    private val fieldMap: Map<Int, Map<Int, Field>> =
        fields.groupBy { it.y }.mapValues { (_, field) -> field.associateBy { it.x } }

    fun down(by: Int): Area = Area(fields.map { it.down(by) })
    fun down(): Area = Area(fields.map { it.down() })
    fun left(): Area = left(1)
    fun left(by: Int): Area = Area(fields.map { it.left(by) })
    fun right(): Area = right(1)

    fun right(by: Int): Area = Area(fields.map { it.right(by) })
    private fun leftSide(): Int = allX().minOrNull() ?: 0
    private fun rightSide(): Int = allX().maxOrNull() ?: 0
    fun top(): Int = allY().minOrNull() ?: 0

    fun bottom(): Int = allY().maxOrNull() ?: 0
    fun width(): Int = (rightSide() - leftSide()) + 1
    fun height(): Int = if (fields.isEmpty()) 0 else (bottom() - top()) + 1

    fun sizeNonEmpty(): Int = nonEmptyFields().count()
    fun widthNonEmpty(): Int = (rightSideNonEmpty() - leftSideNonEmpty()) + 1
    fun leftSideNonEmpty(): Int = nonEmptyFields().map { it.x }.minOrNull() ?: 0
    fun rightSideNonEmpty(): Int = nonEmptyFields().map { it.x }.maxOrNull() ?: 0
    fun bottomNonEmpty(): Int = nonEmptyFields().map { it.y }.maxOrNull() ?: 0

    private fun nonEmptyFields() = fields.filter { it.isFilled() }

    fun state(): List<List<Filling>> =
        (0..bottom()).map { y ->
            (0..rightSide()).map { x ->
                fillingOf(x, y)
            }
        }.toList()

    private fun fillingOf(x: Int, y: Int): Filling = get(x, y).filling
    fun row(y: Int): List<Field> = (0 until width()).map { x -> get(x, y) }
    private fun get(x: Int, y: Int) = fieldMap[y]?.get(x) ?: Field.empty(x, y)
    private fun allY() = fields.map { it.y }
    private fun allX() = fields.map { it.x }
    private fun distance() = Field(leftSide(), top())

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(width()) }
            .map { field -> field.plus(distance()) }
    )

    fun combine(area: Area): Area =
        Area(
            allY()
                .plus(area.allY())
                .flatMap { y ->
                    allX()
                        .plus(area.allX())
                        .map { x -> Field(x, y, Filling.higher(fillingOf(x, y), area.fillingOf(x, y))) }
                }
        )

    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }
    fun collidesWith(field: Field): Boolean = field.collides() && get(field.x, field.y).collides()

    fun move(vector: Field): Area = Area(fields.map { field -> field.plus(vector) })
    fun within(area: Area): Area = Area(fields.filter { it.within(area) })

    fun specials(): Area = fields.fold(this) { area, field -> field.special(field, area) }

    fun fall(): Area {
        val willFall: List<List<Field>> = fields.filter { field ->
            field.falls() && belowIsEmpty(field) && !hasAnchor(field)
        }.map { withConnectedFieldsAbove(it) }
            .toList()

        willFall.map { Field.empty(it[0].x, it[0].y + 1) }
        val below = willFall.associateBy({ Field.empty(it[0].x, it[0].y + 1) }, { it.size })
        return Area(fields
            .map {
                when {
                    willFall.flatten().contains(it) -> it.down()
                    below.keys.contains(it) -> it.up(below[it] ?: 1)
                    else -> it
                }
            })
    }

    private fun withConnectedFieldsAbove(field: Field): List<Field> {
        val result: MutableList<Field> = mutableListOf(field)
        for (y in field.y - 1 downTo top()) {
            if (get(field.x, y).isFilled()) {
                result.add(get(field.x, y))
            } else {
                break
            }
        }
        return result
    }

    private fun hasAnchor(field: Field): Boolean =
        isAnchor(field) || hasAnchorToTheRight(rightOf(field)) || hasAnchorToTheLeft(leftOf(field))

    private fun isAnchor(field: Field) = standsOnSoil(field) || standsOnBottom(field)
    private fun hasAnchorToTheRight(field: Field): Boolean =
        field.falls() && (isAnchor(field) || hasAnchorToTheRight(below(field)) || hasAnchorToTheRight(rightOf(field)))

    private fun hasAnchorToTheLeft(field: Field): Boolean =
        field.falls() && (isAnchor(field) || hasAnchorToTheLeft(below(field)) || hasAnchorToTheLeft(leftOf(field)))

    private fun standsOnSoil(field: Field) = below(field).isSoil()
    private fun standsOnBottom(field: Field) = field.y == height() - 1
    private fun rightOf(field: Field) = get(field.x + 1, field.y)
    private fun leftOf(field: Field) = get(field.x - 1, field.y)
    private fun below(field: Field) = get(field.x, field.y + 1)
    fun above(field: Field) = get(field.x, field.y - 1)
    private fun belowIsEmpty(field: Field) = below(field).filling == Filling.EMPTY

    fun dig(rowsOfSoil: Int): Area {
        val needToDig = (height() - rowsOfSoil until height())
            .map { y -> row(y) }
            .filterNot { row -> row.all { it.isSoil() } }
            .any()

        return if (needToDig) {
            addRowsOfSoil(1)
        } else {
            this
        }
    }

    private fun addRowsOfSoil(amount: Int): Area {
        val cutUpperMostLines = fields.filter { it.y > amount - 1 }
            .map { it.up(amount) }
        val newSoil = (height() - amount until height())
            .flatMap { y ->
                (0 until width()).map { x -> Field.soil(x, y) }
            }
        return Area(cutUpperMostLines + newSoil)
    }

    override fun toString(): String =
        "\n" + state().joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } } + "\n"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Area

        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int = fields.hashCode()
}