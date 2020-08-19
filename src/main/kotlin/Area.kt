open class Area(val fields: List<Field>) {

    constructor(vararg fields: Field) : this(fields.asList())

    fun width(): Int {
        return (rightSide() - leftSide()) + 1
    }

    fun height(): Int {
        val top = fields.map { it.y }.minOrNull()!!
        fields.map { it.y }.maxOrNull()!!
        return (bottom() - top) + 1
    }

    fun bottom(): Int {
        return fields.map { it.y }.maxOrNull()!!
    }

    fun leftSide(): Int {
        return fields.map { it.x }.minOrNull()!!
    }

    fun rightSide(): Int {
        return fields.map { it.x }.maxOrNull()!!
    }

    fun down(): Area = Area(fields.map { field -> field.below() }.toList())

    fun left(): Area = Area(fields.map { field -> field.toTheLeft() }.toList())

    fun right(): Area = Area(fields.map { field -> field.toTheRight() }.toList())

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

    fun covers(field: Field): Boolean {
        return fields.contains(field)
    }


}