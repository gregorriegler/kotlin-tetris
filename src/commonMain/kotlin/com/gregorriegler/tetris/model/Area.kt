package com.gregorriegler.tetris.model

open class Area(fields: List<Field>) : PositionedFrame {

    companion object {
        fun circle(center: Position, radius: Int): Area = Area(
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

    constructor(vararg fields: Field) : this(fields.toList())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: TetrisFrame) : this(frame.rows().flatMap { y -> frame.columns().map { x -> Field.empty(x, y) } })

    val fields: List<Field> = fields.sorted()
    final override val x: Int = this.fields.minOfOrNull { it.x } ?: 0
    final override val y: Int = (this.fields.firstOrNull() ?: Field.empty(0, 0)).y
    final override val rightSide: Int = this.fields.maxOfOrNull { it.x } ?: 0
    final override val bottom: Int = (this.fields.lastOrNull() ?: Field.empty(0, 0)).y
    override val width: Int = rightSide - x + 1
    override val height: Int = bottom - y + 1

    fun down(by: Int): Area = Area(fields.map { it.down(by) })
    fun down(): Area = Area(fields.map { it.down() })
    fun left(): Area = left(1)
    fun left(by: Int): Area = Area(fields.map { it.left(by) })
    fun right(): Area = right(1)
    fun right(by: Int): Area = Area(fields.map { it.right(by) })

    fun sizeNonEmpty(): Int = nonEmptyFields().count()
    fun widthNonEmpty(): Int = (rightSideNonEmpty() - leftSideNonEmpty()) + 1
    fun leftSideNonEmpty(): Int = nonEmptyFields().map { it.x }.minOrNull() ?: 0
    fun rightSideNonEmpty(): Int = nonEmptyFields().map { it.x }.maxOrNull() ?: 0
    fun bottomNonEmpty(): Int = nonEmptyFields().map { it.y }.maxOrNull() ?: 0
    private fun nonEmptyFields() = fields.filter { it.isFilled() }
    fun state(): List<List<Filling>> =
        (0..bottom).map { y ->
            (0..rightSide).map { x ->
                get(x, y).filling
            }
        }.toList()

    private fun row(y: Int): List<Field> = (0 until width).map { x -> get(x, y) }

    fun get(x: Int, y: Int): Field {
        if (x < this.x || y < this.y || x > rightSide || y > bottom) return Field.empty(x, y)
        return fields.getOrNull((x - this.x) + ((y - this.y) * width)) ?: Field.empty(x, y)
    }

    private fun allY() = fields.map { it.y }.distinct()
    private fun allX() = fields.map { it.x }.distinct()
    private fun distance() = Field(x, y)

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(width) }
            .map { field -> field.plus(distance()) }
    )

    fun combine(area: Area): Area =
        Area(
            allY().plus(area.allY()).distinct()
                .flatMap { y ->
                    allX().plus(area.allX()).distinct()
                        .map { x -> Field(x, y, Filling.higher(get(x, y).filling, area.get(x, y).filling)) }
                }
        )

    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }
    fun collidesWith(field: Field): Boolean = field.collides() && get(field.x, field.y).collides()

    fun move(vector: Field): Area = Area(fields.map { field -> field.plus(vector) })
    fun within(area: Area): Area = Area(fields.filter { it.within(area) })

    fun specials(): Area = fields.fold(this) { area, field -> field.special(area) }

    fun fall(): Area {
        val willFall: List<List<Field>> = fields.filter { field ->
            field.falls() && belowIsEmpty(field) && !hasAnchor(field)
        }.map { withConnectedFieldsAbove(it) }
            .toList()

        willFall.map { Field.empty(it[0].x, it[0].y + 1) }
        val below = willFall.associateBy({ Field.empty(it[0].x, it[0].y + 1) }, { it.size })
        return Area(
            fields.map {
                when {
                    willFall.flatten().contains(it) -> it.down()
                    below.keys.contains(it) -> it.up(below[it] ?: 1)
                    else -> it
                }
            })
    }

    private fun withConnectedFieldsAbove(field: Field): List<Field> {
        val result: MutableList<Field> = mutableListOf(field)
        for (y in field.y - 1 downTo y) {
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
    private fun standsOnBottom(field: Field) = field.y == height - 1
    private fun rightOf(field: Field) = get(field.x + 1, field.y)
    private fun leftOf(field: Field) = get(field.x - 1, field.y)
    private fun below(field: Field) = get(field.x, field.y + 1)
    fun above(field: Field) = get(field.x, field.y - 1)
    private fun belowIsEmpty(field: Field) = below(field).filling == Filling.EMPTY

    fun dig(rowsOfSoil: Int): Area {
        val needToDig = (height - rowsOfSoil until height)
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
        val newSoil = (height - amount until height)
            .flatMap { y ->
                (0 until width).map { x -> Field.soil(x, y) }
            }
        return Area(cutUpperMostLines + newSoil)
    }


    fun erase(area: Area): Area = erase(area.fields)

    private fun erase(fields: List<Field>): Area = Area(this.fields.map {
        if (it.collidesWith(fields) || (it.isSoil() && above(it).collidesWith(fields))) {
            it.erase()
        } else {
            it
        }
    })

    fun eraseFilledRows(): Pair<Area, Int> {
        val filledRows = filledRows()
        val remainingArea = erase(filledRows)
        return Pair(remainingArea, filledRows.size)
    }

    private fun filledRows(): List<Field> {
        return (y..bottom)
            .filter { y -> row(y).all { it.isFilled() || it.isSoil() } }
            .filterNot { y -> row(y).all { it.isSoil() } }
            .flatMap { y ->
                row(y).filterNot { it.isSoil() }
                    .map { field -> Field(field.x, field.y, field.filling) }
            }
    }

    override fun toString(): String =
        "\n" + state().joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } } + "\n"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Area

        if (fields.toSet() != other.fields.toSet()) return false

        return true
    }

    override fun hashCode(): Int = fields.toSet().hashCode()
}