import kotlin.math.roundToInt

class Frame(
    val width: Int,
    private val height: Int,
) {
    fun empty(): List<List<String>> =
        (0 until height)
            .map { (0 until width).map { Field.EMPTY }.toList() }
            .toList()

    fun startingField(): Field = Field(center(), -1)

    fun startingArea(structure: Structure): Area {
        val top = 0 - structure.height()
        val left = (width - structure.width()) / 2
        return Area(structure.fields.map { field -> field.plus(Field(left, top)) }.toList())
    }

    fun topCenter(): Field = Field(center(), 0)

    fun leftOf(field: Field): Field =
        if (field.x > 0)
            field.toTheLeft()
        else
            field

    fun left(area: Area): Area =
        if(area.leftSide() > 0)
            area.left()
        else
            area

    fun rightOf(field: Field): Field =
        if (field.x < width - 1)
            field.toTheRight()
        else
            field

    fun right(area: Area): Area =
        if(area.rightSide() < width - 1)
            area.right()
        else
            area

    fun below(field: Field): Field =
        if (field.y < height - 1)
            field.below()
        else
            field
    fun down(area: Area): Area =
        if (area.bottom() < height - 1)
             area.down()
        else
            area
    fun isAtBottom(field: Field): Boolean = field.y == height - 1
    private fun center() = width.toDouble().div(2).roundToInt() - 1
}