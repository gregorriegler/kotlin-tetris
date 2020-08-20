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

    fun left(area: Area): Area =
        if(area.leftSide() > 0)
            area.left()
        else
            area

    fun right(area: Area): Area =
        if(area.rightSide() < width - 1)
            area.right()
        else
            area

    fun down(area: Area): Area =
        if (area.bottom() < height - 1)
             area.down()
        else
            area

    fun isAtBottom(area: Area): Boolean = area.bottom() == height - 1

    private fun center() = width.toDouble().div(2).roundToInt() - 1
}