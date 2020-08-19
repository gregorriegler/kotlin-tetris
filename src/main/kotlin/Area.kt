open class Area(val fields: List<Field>) {

    constructor(vararg fields: Field) : this(fields.asList())

    companion object {
        fun create2by2(): Area {
            return Area(
                Field(0, 0),
                Field(1, 0),
                Field(0, 1),
                Field(1, 1)
            )
        }
    }

    fun width(): Int {
        val left = fields.map { it.x }.minOrNull()!!
        val right = fields.map { it.x }.maxOrNull()!!
        return (right - left) + 1
    }

    fun height(): Int {
        val top = fields.map { it.y }.minOrNull()!!
        val bottom = fields.map { it.y }.maxOrNull()!!
        return (bottom - top) + 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int {
        return fields.hashCode()
    }

    override fun toString(): String {
        return "Area(fields=$fields)"
    }


}