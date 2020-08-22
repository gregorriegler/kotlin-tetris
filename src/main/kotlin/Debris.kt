class Debris(
    private var debris: Area,
) {
    constructor(frame: Frame) : this(Area(frame))
    constructor(debris: String) : this(Area(debris))

    fun add(stone: Stone) {
        debris = debris.combine(stone.area)
    }

    fun dissolveFilledRows(): Int {
        val removed = debris.dissolveFilledRows()
        debris = removed.first
        return removed.second
    }

    fun width(): Int = debris.width()
    fun height(): Int = debris.height()

    fun collidesWith(area: Area): Boolean = debris.collides(area)
    fun collidesWith(field: Field): Boolean = debris.collides(field)

    fun stateWithStone(stone: Stone): List<List<Filling>> =
        debris.combine(stone.area)
            .within(debris)
            .state()

    override fun toString(): String = "\n" + Area.draw(debris.state()) + "\n"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Debris

        if (debris != other.debris) return false

        return true
    }

    override fun hashCode(): Int = debris.hashCode()
}