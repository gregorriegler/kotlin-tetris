import Filling.EMPTY
import Filling.FILLED

class Field(
    val x: Int,
    val y: Int,
    val filling: Filling
) {

    companion object {
        fun filled(x: Int, y: Int): Field {
            return Field(x, y, FILLED)
        }

        fun empty(x: Int, y: Int): Field {
            return Field(x, y, EMPTY)
        }
    }

    constructor(x: Int, y: Int) : this(x, y, EMPTY)

    constructor(x: Int, y: Int, filling: String) : this(x, y, Filling.of(filling))

    fun filled(): Field = filled(x, y)

    fun down(): Field = Field(x, y + 1, filling)

    fun left(): Field = Field(x - 1, y, filling)

    fun right(): Field = Field(x + 1, y, filling)

    fun rotate(width: Int): Field {
        return Field(width - y - 1, x, filling)
    }

    fun plus(field: Field): Field {
        return Field(x + field.x, y + field.y, filling)
    }

    fun minus(field: Field): Field {
        return Field(x - field.x, y - field.y, filling)
    }

    fun isFilled(): Boolean = filling == FILLED

    override fun toString(): String = "($x,$y)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Field

        if (x != other.x) return false
        if (y != other.y) return false
        if (filling != other.filling) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + filling.hashCode()
        return result
    }
}