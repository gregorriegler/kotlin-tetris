import kotlin.math.roundToInt

class Frame(
    val width: Int,
    val height: Int,
) {
    fun empty(): List<List<String>> =
        (0 until height)
            .map { (0 until width).map { Filling.EMPTY.toString() }.toList() }
            .toList()

    fun startingArea(structure: Structure): Area {
        val top = 0 - structure.height()
        val left = (width - structure.width()) / 2
        val vector = Field(left, top)
        return Area( // this is area.move
            structure.fields
                .map { field -> field.plus(vector) }
                .toSet()
        )
    }

    fun topCenter(): Field = Field(center(), 0)

    fun left(area: Area): Area =
        if (isAtLeftBorder(area))
            area
        else
            area.left()

    fun left(area: Area, debris: Debris): Area =
        if (isAtLeftBorder(area) || debris.isAt(area.left()))
            area
        else
            area.left()

    fun right(area: Area): Area =
        if (area.rightSideOfFilled() < width - 1)
            area.right()
        else
            area

    fun right(area: Area, debris: Debris): Area =
        if (isAtRightBorder(area) || debris.isAt(area.right()))
            area
        else
            area.right()

    fun down(area: Area): Area =
        if (area.bottomOfFilled() < height - 1)
            area.down()
        else
            area

    private fun isAtLeftBorder(area: Area) = area.leftSideOfFilled() <= 0

    private fun isAtRightBorder(area: Area) = area.rightSideOfFilled() >= width - 1

    fun isAtBottom(area: Area): Boolean = area.bottomOfFilled() == height - 1

    private fun center() = width.toDouble().div(2).roundToInt() - 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Frame

        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}