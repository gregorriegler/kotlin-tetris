import kotlin.math.roundToInt

class Frame(
    val width: Int,
    private val height: Int,
) {
    fun empty(): List<List<String>> {
        return (0 until height)
            .map { (0 until width).map { Field.EMPTY }.toList() }
            .toList()
    }

    fun center() = width.toDouble().div(2).roundToInt() - 1

    fun isBottom(y: Int) = y == height - 1

    fun leftOf(field: Field): Field {
        return if (field.x > 0)
            field.toTheLeft()
        else
            field
    }

    fun rightOf(field: Field): Field {
        return if (field.x < width - 1)
            field.toTheRight()
        else
            field
    }
}