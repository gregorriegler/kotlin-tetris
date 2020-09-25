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
    constructor(frame: TetrisFrame) : this(frame.rows().flatMap { y ->
        frame.columns().map { x -> Field.empty(x, y) }
    })

    val fields: List<Field> = fields.sorted()
    final override val x: Int = this.fields.minOfOrNull { it.x } ?: 0
    final override val y: Int = (this.fields.firstOrNull() ?: Field.empty(0, 0)).y
    final override val rightSide: Int = this.fields.maxOfOrNull { it.x } ?: 0
    final override val bottom: Int = (this.fields.lastOrNull() ?: Field.empty(0, 0)).y
    override val width: Int = rightSide - x + 1
    override val height: Int = bottom - y + 1

    override fun down(): Area = Area(fields.map { it.down() })
    override fun downBy(amount: Int): Area = Area(fields.map { it.downBy(amount) })
    override fun left(): Area = leftBy(1)
    override fun leftBy(amount: Int): Area = Area(fields.map { it.leftBy(amount) })
    override fun right(): Area = rightBy(1)
    override fun rightBy(amount: Int): Area = Area(fields.map { it.rightBy(amount) })
    fun below(position: Position) = get(position.down())
    fun above(position: Position) = get(position.up())

    fun sizeFalling(): Int = fallingFields().count()
    fun widthFalling(): Int = (rightSideFalling() - leftSideFalling()) + 1
    fun leftSideFalling(): Int = fallingFields().map { it.x }.minOrNull() ?: 0
    fun rightSideFalling(): Int = fallingFields().map { it.x }.maxOrNull() ?: 0
    fun bottomFalling(): Int = fallingFields().map { it.y }.maxOrNull() ?: 0
    private fun fallingFields() = fields.filter { it.falls() }
    fun state(): List<List<Filling>> =
        (0..bottom).map { y ->
            (0..rightSide).map { x ->
                get(x, y).filling
            }
        }.toList()

    private fun row(y: Int): List<Field> = (0 until width).map { x -> get(x, y) }

    private fun indexOf(position: Position) = (position.x - this.x) + ((position.y - this.y) * width)

    private fun isOutside(position: Position): Boolean =
        position.x < this.x || position.y < this.y || position.x > rightSide || position.y > bottom

    private fun allY() = fields.map { it.y }.distinct()
    private fun allX() = fields.map { it.x }.distinct()
    private fun distance() = Position.of(x, y)

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
                                x, y, Filling.higher(
                                    get(x, y).filling,
                                    area.get(x, y).filling
                                )
                            )
                        }
                }
        )

    fun get(x: Int, y: Int) = get(Position.of(x, y))
    fun get(position: Position): Field =
        if (isOutside(position)) {
            Field.empty(position)
        } else {
            fields.getOrNull(indexOf(position)) ?: Field.empty(position)
        }

    fun collidesWith(area: Area): Boolean = fields.any { area.collidesWith(it) }
    fun collidesWith(field: Field): Boolean = field.collides() && get(field).collides()

    fun move(vector: Position): Area = Area(fields.map { field -> field.plus(vector) })
    fun within(area: Area): Area = Area(fields.filter { it.within(area) })

    fun specials(): Score = fields.fold(
        Score(this, 0)
    ) { previous, field -> field.special(previous.area).plus(previous.score) }

    fun fall(): Area {
        val fallingStacks: List<List<Field>> = fields.filter(this::willFall)
            .map(this::verticalStack)
            .toList()
        val roomToFall = fallingStacks.associateBy({ Field.empty(it[0].x, it[0].y + 1) }, { it.size })
        return Area(
            fields.map {
                when {
                    fallingStacks.any { stack -> stack.contains(it) } -> it.down()
                    roomToFall.keys.contains(it) -> it.upBy(roomToFall[it] ?: 1)
                    else -> it
                }
            })
    }

    private fun willFall(field: Field): Boolean = field.falls() && below(field).isEmpty() && !hasAnchor(field)

    private fun hasAnchor(field: Field) = AnchorFinder(this).hasAnchor(field)

    private fun verticalStack(it: Field) = VerticalStackIterator(this, it).asSequence().toList()

    fun dig(amount: Int, coinPercentage: Int): DigResult =
        if (bottomRows(amount).any { notCompletelySoil(it) }) {
            DigResult(addRowsOfSoil(1, coinPercentage), 1)
        } else {
            DigResult(this, 0)
        }

    private fun notCompletelySoil(row: List<Field>) = row.any { !it.isSoilOrCoin() }

    private fun bottomRows(amount: Int) = (height - amount until height).map { row(it) }

    private fun addRowsOfSoil(amount: Int, coinPercentage: Int): Area {
        val upperRows = allRowsExceptTop(amount).map { it.upBy(amount) }
        val soilRows = (height - amount until height).flatMap { this.createRowOfSoilOrCoin(it, coinPercentage) }
        return Area(upperRows + soilRows)
    }

    private fun createRowOfSoilOrCoin(y: Int, coinPercentage: Int): List<Field> =
        (0 until width).map { x -> Field.soilOrCoin(x, y, coinPercentage) }

    private fun allRowsExceptTop(amount: Int) = fields.filter { it.y >= amount }

    fun eraseFilledRows(): Score = erase(filledRows())
    fun erase(area: String): Score = erase(Area(area))
    fun erase(area: Area): Score = erase(area.fields)

    private fun erase(fields: List<Field>): Score = Score(Area(this.fields.map { field ->
        if (field.collidesWith(fields) || (field.isSoilOrCoin() && above(field).collidesWith(fields))) {
            field.erase()
        } else {
            field
        }
    }), fields.size)

    private fun filledRows(): List<Field> {
        return (y..bottom)
            .map { row(it) }
            .filter { it.all { it.falls() || it.isSoilOrCoin() } }
            .filterNot { it.all { it.isSoilOrCoin() } }
            .flatMap { it.filterNot { it.isSoilOrCoin() } }
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
