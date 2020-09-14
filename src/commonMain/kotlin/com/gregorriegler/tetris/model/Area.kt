package com.gregorriegler.tetris.model

open class Area(fields: List<Field>) : PositionedFrame {

    companion object {
        fun circle(center: MovablePosition, radius: Int): Area = Area(
            (center.y - radius..center.y + radius)
                .flatMap { y ->
                    (center.x - radius..center.x + radius)
                        .filter { x ->
                            val dx = x - center.x
                            val dy = y - center.y
                            (dx * dx + dy * dy <= radius * radius)
                        }.map { x -> Field.filled(MovablePosition(x, y)) }
                })

        fun parseFields(string: String): List<Field> = string.trimIndent()
            .split("\n")
            .flatMapIndexed { y, row ->
                val pulls = row.filter { it == Filling.PULL_VALUE }.count() * 2
                row.toCharArray()
                    .withIndex()
                    .filterNot { it.value == Filling.INDENT_VALUE || it.value == Filling.PULL_VALUE }
                    .map { Field(MovablePosition(it.index - pulls, y), it.value) }
            }
    }

    constructor(vararg fields: Field) : this(fields.toList())
    constructor(string: String) : this(parseFields(string))
    constructor(frame: TetrisFrame) : this(frame.rows().flatMap { y ->
        frame.columns().map { x -> Field.empty(MovablePosition(x, y)) }
    })

    val fields: List<Field> = fields.sorted()
    final override val x: Int = this.fields.minOfOrNull { it.x } ?: 0
    final override val y: Int = (this.fields.firstOrNull() ?: Field.empty(MovablePosition(0, 0))).y
    final override val rightSide: Int = this.fields.maxOfOrNull { it.x } ?: 0
    final override val bottom: Int = (this.fields.lastOrNull() ?: Field.empty(MovablePosition(0, 0))).y
    override val width: Int = rightSide - x + 1
    override val height: Int = bottom - y + 1

    fun down(by: Int): Area = Area(fields.map { it.downBy(by) })
    fun down(): Area = Area(fields.map { it.down() })
    fun left(): Area = left(1)
    fun left(by: Int): Area = Area(fields.map { it.leftBy(by) })
    fun right(): Area = right(1)
    fun right(by: Int): Area = Area(fields.map { it.rightBy(by) })
    fun below(position: MovablePosition) = get(position.down())
    fun above(position: MovablePosition) = get(position.up())

    fun sizeNonEmpty(): Int = nonEmptyFields().count()
    fun widthNonEmpty(): Int = (rightSideNonEmpty() - leftSideNonEmpty()) + 1
    fun leftSideNonEmpty(): Int = nonEmptyFields().map { it.x }.minOrNull() ?: 0
    fun rightSideNonEmpty(): Int = nonEmptyFields().map { it.x }.maxOrNull() ?: 0
    fun bottomNonEmpty(): Int = nonEmptyFields().map { it.y }.maxOrNull() ?: 0
    private fun nonEmptyFields() = fields.filter { it.isFilled() }
    fun state(): List<List<Filling>> =
        (0..bottom).map { y ->
            (0..rightSide).map { x ->
                get(MovablePosition(x, y)).filling
            }
        }.toList()

    private fun row(y: Int): List<Field> = (0 until width).map { x -> get(MovablePosition(x, y)) }

    fun get(position: MovablePosition): Field =
        if (isOutside(position)) {
            Field.empty(position)
        } else {
            fields.getOrNull(indexOf(position)) ?: Field.empty(position)
        }

    private fun indexOf(position: MovablePosition) = (position.x - this.x) + ((position.y - this.y) * width)

    private fun isOutside(position: MovablePosition): Boolean =
        position.x < this.x || position.y < this.y || position.x > rightSide || position.y > bottom

    private fun allY() = fields.map { it.y }.distinct()
    private fun allX() = fields.map { it.x }.distinct()
    private fun distance() = MovablePosition(x, y)

    fun plus(other: Area): Area {
        return Area(fields + other.fields)
    }

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
                        .map { x ->
                            Field(
                                MovablePosition(x, y), Filling.higher(
                                    get(MovablePosition(x, y)).filling,
                                    area.get(MovablePosition(x, y)).filling
                                )
                            )
                        }
                }
        )

    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }
    fun collidesWith(field: Field): Boolean = field.collides() && get(field.position).collides()

    fun move(vector: MovablePosition): Area = Area(fields.map { field -> field.plus(vector) })
    fun within(area: Area): Area = Area(fields.filter { it.within(area) })

    fun specials(): Area = fields.fold(this) { area, field -> field.special(area) }

    fun fall(): Area {
        val fallingStacks: List<List<Field>> = fields.filter(this::willFall)
            .map(this::verticalStack)
            .toList()
        val roomToFall = fallingStacks.associateBy({ Field.empty(MovablePosition(it[0].x, it[0].y + 1)) }, { it.size })
        return Area(
            fields.map {
                when {
                    fallingStacks.any { stack -> stack.contains(it) } -> it.down()
                    roomToFall.keys.contains(it) -> it.upBy(roomToFall[it] ?: 1)
                    else -> it
                }
            })
    }

    private fun willFall(field: Field): Boolean = field.falls() && below(field.position).isEmpty()
            && !AnchorFinder(this).hasAnchor(field.position)

    private fun verticalStack(it: Field) = VerticalStackIterator(this, it).asSequence().toList()

    fun dig(amount: Int): Pair<Area, Int> =
        if ((height - amount until height).any { !isRowOfSoil(it) }) {
            Pair(addRowsOfSoil(1), 1)
        } else {
            Pair(this, 0)
        }

    private fun isRowOfSoil(y: Int): Boolean = row(y).all { it.isSoil() }

    private fun addRowsOfSoil(amount: Int): Area =
        Area(
            allRowsExceptTop(amount).map { it.upBy(amount) }
                    + (height - amount until height).flatMap(this::createRowOfSoil)
        )

    private fun createRowOfSoil(y: Int): List<Field> = createRow(y, Field.Companion::soil)
    private fun createRow(y: Int, field: (MovablePosition) -> Field): List<Field> =
        (0 until width).map { x -> field(MovablePosition(x, y)) }

    private fun allRowsExceptTop(amount: Int) = fields.filter { it.y >= amount }

    fun eraseFilledRows(): Pair<Area, Int> {
        val filledRows = filledRows()
        val remainingArea = erase(filledRows)
        return Pair(remainingArea, filledRows.size)
    }

    fun erase(area: Area): Area = erase(area.fields)

    private fun erase(fields: List<Field>): Area = Area(this.fields.map { field ->
        if (field.collidesWith(fields) || (field.isSoil() && above(field.position).collidesWith(fields))) {
            field.erase()
        } else {
            field
        }
    })

    private fun filledRows(): List<Field> {
        return (y..bottom)
            .filter { y -> row(y).all { it.isFilled() || it.isSoil() } }
            .filterNot { y -> isRowOfSoil(y) }
            .flatMap { y -> row(y).filterNot { it.isSoil() } }
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
