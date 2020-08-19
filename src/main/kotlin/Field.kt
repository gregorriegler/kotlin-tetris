class Field(
    val x: Int,
    val y: Int,
) {
    companion object {
        const val EMPTY = "_"
    }

    fun below(): Field = Field(x, y + 1)

    fun toTheLeft(): Field = Field(x - 1, y)

    fun toTheRight(): Field = Field(x + 1, y)

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