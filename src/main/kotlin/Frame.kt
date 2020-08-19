import kotlin.math.roundToInt

class Frame(
    val width: Int,
    private val height: Int,
) {
    fun empty(): List<List<String>> = (0 until height)
        .map { (0 until width).map { Field.EMPTY }.toList() }
        .toList()

    fun center() = width.toDouble().div(2).roundToInt() - 1

    fun startingField(): Field = Field(center(), -1)

    fun leftOf(field: Field): Field =
        if (field.x > 0)
            field.toTheLeft()
        else
            field

    fun rightOf(field: Field): Field =
        if (field.x < width - 1)
            field.toTheRight()
        else
            field

    fun isAtBottom(field: Field): Boolean = field.y == height - 1
}