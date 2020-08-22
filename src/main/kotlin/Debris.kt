class Debris(
    private val frame: Frame,
    private var debrisNew: Area,
) {
    constructor(frame: Frame) : this(frame, Area(frame))
    constructor(debris: String) : this(
        Frame(
            debris.trimIndent().substringBefore('\n').length,
            debris.trimIndent().count { it == '\n' } + 1
        ),
        Area(debris)
    )

    fun add(stone: Stone) {
        debrisNew = debrisNew.combine(stone.area)
    }

    fun dissolveFilledRows(): Int {
        val removed = debrisNew.removeFilledLines()
        debrisNew = removed.first
        return removed.second
    }

    fun width(): Int = frame.width
    fun height(): Int = frame.height

    fun isAt(area: Area): Boolean = debrisNew.overlaps(area)
    fun isAt(field: Field): Boolean = debrisNew.has(Field.filled(field.x, field.y))

    fun stateWithStone(stone: Stone): List<List<Filling>> =
        debrisNew.combine(stone.area)
            .within(frame)
            .state()

    override fun toString(): String = "\n" + Tetris.draw(debrisNew.state()) + "\n"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Debris

        if (frame != other.frame) return false
        if (debrisNew != other.debrisNew) return false

        return true
    }

    override fun hashCode(): Int {
        var result = frame.hashCode()
        result = 31 * result + debrisNew.hashCode()
        return result
    }
}