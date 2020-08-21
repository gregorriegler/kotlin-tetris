class Field(
    val x: Int,
    val y: Int,
) {
    companion object {
        const val EMPTY = "_"
        const val STONE = "#"
    }

    fun below(): Field = Field(x, y + 1)

    fun toTheLeft(): Field = Field(x - 1, y)

    fun toTheRight(): Field = Field(x + 1, y)

    fun rotate(width: Int): Field {
        return Field(width - y - 1, x)
    }

    fun plus(field: Field): Field {
        return Field(x + field.x, y + field.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Field

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
    override fun toString(): String = "($x,$y)"
}