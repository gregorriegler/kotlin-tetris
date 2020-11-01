package com.gregorriegler.tetris.model

open class Grid(fields: List<Field>) : PositionedFrame, Collidable {

    companion object {
        fun circle(center: Position, radius: Int): Grid = Grid(
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

    override fun down(): Grid = Grid(fields.map { it.down() })
    override fun downBy(amount: Int): Grid = Grid(fields.map { it.downBy(amount) })
    override fun left(): Grid = leftBy(1)
    override fun leftBy(amount: Int): Grid = Grid(fields.map { it.leftBy(amount) })
    override fun right(): Grid = rightBy(1)
    override fun rightBy(amount: Int): Grid = Grid(fields.map { it.rightBy(amount) })
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

    fun asStones(display: Frame, depth: Int, palette: Palette): List<TetrisStone> {
        return fields
            .filterNot { it.isEmpty() }
            .map { TetrisStone.of(display, this, it, depth, palette) }
    }

    private fun row(y: Int): List<Field> = (0 until width).map { x -> get(x, y) }

    private fun indexOf(position: Position) = (position.x - this.x) + ((position.y - this.y) * width)

    private fun isOutside(position: Position): Boolean =
        position.x < this.x || position.y < this.y || position.x > rightSide || position.y > bottom

    private fun allY() = fields.map { it.y }.distinct()
    private fun allX() = fields.map { it.x }.distinct()
    private fun distance() = Position.of(x, y)

    fun rotate(): Grid = Grid(
        fields.map { field -> field.minus(distance()) }
            .map { field -> field.rotate(width) }
            .map { field -> field.plus(distance()) }
    )

    fun combine(grid: Grid): Grid =
        Grid(
            allY().plus(grid.allY()).distinct()
                .flatMap { y ->
                    allX().plus(grid.allX()).distinct()
                        .map { x ->
                            Field(
                                x, y, Filling.higher(
                                    get(x, y).filling,
                                    grid.get(x, y).filling
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

    fun collidesWith(subject: Collidable): Boolean = fields.any { subject.collidesWith(it) }
    override fun collidesWith(field: Field): Boolean = field.collides() && get(field).collides()

    fun move(vector: Position): Grid = Grid(fields.map { field -> field.plus(vector) })
    fun within(grid: Grid): Grid = Grid(fields.filter { it.within(grid) })

    fun fall(): Grid {
        val fallingStacks: List<List<Field>> = fields.filter(this::willFall)
            .map(this::verticalStack)
            .toList()
        val roomToFall = fallingStacks.associateBy({ Field.empty(it[0].x, it[0].y + 1) }, { it.size })
        return Grid(
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

    private fun addRowsOfSoil(amount: Int, coinPercentage: Int): Grid {
        val upperRows = allRowsExceptTop(amount).map { it.upBy(amount) }
        val soilRows = (height - amount until height).flatMap { this.createRowOfSoilOrCoin(it, coinPercentage) }
        return Grid(upperRows + soilRows)
    }

    private fun createRowOfSoilOrCoin(y: Int, coinPercentage: Int): List<Field> =
        (0 until width).map { x -> Field.soilOrCoin(x, y, coinPercentage) }

    private fun allRowsExceptTop(amount: Int) = fields.filter { it.y >= amount }

    fun filledRowsAndSoilBelow(): List<Field> {
        return filledRows().flatMap {
            if (below(it).isSoilOrCoin()) {
                listOf(below(it), it)
            } else {
                listOf(it)
            }
        }
    }

    private fun filledRows(): List<Field> {
        return (y..bottom)
            .map { row(it) }
            .filter { it.all { it.falls() || it.isSoilOrCoin() } }
            .filterNot { it.all { it.isSoilOrCoin() } }
            .flatMap { it.filterNot { it.isSoilOrCoin() } }
    }

    fun erase(grid: Grid): EraseResult = erase(grid.fields)
    fun eraseFilledRows(): EraseResult = erase(filledRowsAndSoilBelow())
    fun specials(): EraseResult = fields.fold(
        EraseResult(this, 0)
    ) { previous, field -> field.special(previous.grid).plus(previous.score) }

    private fun erase(fields: List<Field>): EraseResult {
        val fieldScores = this.fields.map { it.erase(fields) }
        return EraseResult(Grid(fieldScores.map { it.field }), fieldScores.sumOf { it.score })
    }

    override fun toString(): String =
        "\n" + state().joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } } + "\n"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Grid

        if (fields.toSet() != other.fields.toSet()) return false

        return true
    }

    override fun hashCode(): Int = fields.toSet().hashCode()
}
