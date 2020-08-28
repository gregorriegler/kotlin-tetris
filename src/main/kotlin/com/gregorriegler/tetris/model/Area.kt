package com.gregorriegler.tetris.model

open class Area(val fields: Set<Field>) {

    companion object {
        fun circle(center: Field, radius: Int): Area {
            return Area(
                (center.y - radius .. center.y + radius)
                    .flatMap { y ->
                        (center.x - radius .. center.x + radius)
                            .filter { x ->
                                val dx = x - center.x
                                val dy = y - center.y
                                (dx * dx + dy * dy <= radius * radius)
                            }.map { x -> Field.filled(x, y) }
                    })
        }

        fun parseFields(string: String): List<Field> {
            return string.trimIndent()
                .split("\n")
                .flatMapIndexed { y, row ->
                    val pulls = row.filter { it == Filling.PULL_VALUE }.count() * 2
                    row.toCharArray()
                        .withIndex()
                        .filterNot { it.value == Filling.INDENT_VALUE || it.value == Filling.PULL_VALUE }
                        .map { Field(it.index - pulls, y, it.value) }
                }
        }

        fun draw(combined: List<List<Filling>>) =
            combined.joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } }
    }

    constructor(vararg fields: Field) : this(fields.toSet())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: Frame) : this(
        frame.rows().flatMap { y -> frame.columns().map { x -> Field.empty(x, y) } }
    )

    constructor(fields: List<Field>) : this(fields.toSet())

    private val fieldMap: Map<Int, Map<Int, Field>> =
        fields.groupBy { it.y }.mapValues { (_, field) -> field.associateBy { it.x } }

    fun down(by: Int): Area = Area(fields.map { it.down(by) })
    fun down(): Area = Area(fields.map { it.down() })
    fun left(): Area = left(1)
    fun left(by: Int): Area = Area(fields.map { it.left(by) })
    fun right(): Area = right(1)

    fun right(by: Int): Area = Area(fields.map { it.right(by) })
    private fun leftSide(): Int = fields.map { it.x }.minOrNull() ?: 0
    private fun rightSide(): Int = fields.map { it.x }.maxOrNull() ?: 0
    private fun top(): Int = fields.map { it.y }.minOrNull() ?: 0

    private fun bottom(): Int = fields.map { it.y }.maxOrNull() ?: 0
    fun width(): Int = (rightSide() - leftSide()) + 1
    fun height(): Int = if (fields.isEmpty()) 0 else (bottom() - top()) + 1

    fun size(): Int = fields.filter { it.isFilled() }.count()
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

    private fun fillingOf(x: Int, y: Int): Filling = get(x, y).filling

    private fun get(x: Int, y: Int) = fieldMap[y]?.get(x) ?: Field.empty(x, y)

    private fun distance() = Field(leftSide(), top())

    fun rotate(): Area = Area(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(width()) }
            .map { field -> field.plus(distance()) }
    )

    fun combine(area: Area): Area =
        Area(fields.map { it.y }.plus(area.fields.map { it.y })
            .flatMap { y ->
                fields.map { it.x }.plus(area.fields.map { it.x }).map { x ->
                    Field(x, y, Filling.higher(fillingOf(x, y), area.fillingOf(x, y)))
                }
            })

    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }

    fun collidesWith(field: Field): Boolean = field.collides() && get(field.x, field.y).collides()

    fun aboveCentered(area: Area): Area = move(Field(
        (area.width() - width()) / 2,
        area.top() - height()
    ))

    private fun move(vector: Field): Area = Area(fields.map { field -> field.plus(vector) })

    fun within(area: Area): Area = Area(fields.filter { it.within(area) })

    fun erase(area: Area): Area = Area(fields.map {
        if (area.collidesWith(it)) {
            it.erase()
        } else {
            it
        }
    })

    fun eraseFilledRows(): Pair<Area, Int> {
        val filledRows = filledRows()
        val remaining = erase(filledRows)
        return Pair(remaining, filledRows.size())
    }

    fun specials(): Area {
        val bombs = fields.filter { it.filling == Filling.BOMB }.toList()
        var result: Area = this

        for (bomb in bombs) {
            result = explode(bomb)
        }

        return result
    }

    private fun explode(it: Field): Area {
        return erase(circle(it, 4))
    }

    fun fall(): Area {
        val willFall: List<List<Field>> = fields.filter { field ->
            field.isFilled()
                    && belowIsEmpty(field)
                    && !isAnchor(field)
                    && !hasAnchorToTheRight(rightOf(field))
                    && !hasAnchorToTheLeft(leftOf(field))
        }.map { withConnectedFieldsAbove(it) }
            .toList()

        willFall.map { Field.empty(it[0].x, it[0].y + 1) }
        val below = willFall.associateBy({ Field.empty(it[0].x, it[0].y + 1) }, { it.size })
        val result = fields
            .map {
                when {
                    willFall.flatten().contains(it) -> it.down()
                    below.keys.contains(it) -> it.up(below[it] ?: 1)
                    else -> it
                }
            }
        return Area(result)
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

    // todo: can duplicate check many fields (need to remember fields that have already been checked)
    private fun hasAnchorToTheRight(field: Field): Boolean =
        field.isFilled() && (isAnchor(field) || hasAnchorToTheRight(below(field)) || hasAnchorToTheRight(rightOf(field)))

    // todo: can duplicate check many fields (need to remember fields that have already been checked)
    private fun hasAnchorToTheLeft(field: Field): Boolean =
        field.isFilled() && (isAnchor(field) || hasAnchorToTheLeft(below(field)) || hasAnchorToTheLeft(leftOf(field)))

    private fun isAnchor(field: Field) = standsOnSoil(field) || (field.isFilled() && isAtBottom(field))
    private fun standsOnSoil(field: Field) = below(field).isSoil()
    private fun rightOf(field: Field) = get(field.x + 1, field.y)
    private fun leftOf(field: Field) = get(field.x - 1, field.y)
    private fun below(field: Field) = get(field.x, field.y + 1)
    private fun belowIsEmpty(field: Field) =
        get(field.x, field.y + 1).filling == Filling.EMPTY

    private fun isAtBottom(field: Field) = field.y == height() - 1

    fun filledRows(): Area {
        return Area(
            (top()..bottom())
                .filter { y -> row(y).all { it.isFilled() || it.isSoil() } }
                .filterNot { y -> row(y).all { it.isSoil() } }
                .flatMap { y ->
                    row(y).filterNot { it.isSoil() }
                        .map { field -> Field(field.x, field.y, field.filling) }
                }
        )
    }

    fun dig(rowsOfSoil: Int): Area {
        val needToDig = (height() - rowsOfSoil until height())
            .map { y -> row(y) }
            .filterNot { it.all { it.isSoil() } }
            .any()

        return if (needToDig) {
            addRowsOfSoil(1)
        } else {
            this
        }
    }

    private fun addRowsOfSoil(amount: Int): Area {
        val cutUpperLines = fields.filter { it.y > amount - 1 }
            .map { it.up(amount) }
        val filledLinesForBottom = (height() - amount until height()).flatMap { y ->
            (0 until width()).map { x -> Field.soil(x, y) }
        }
        return Area(cutUpperLines + filledLinesForBottom)
    }

    private fun row(y: Int): List<Field> = (0 until width()).map { x -> get(x, y) }

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