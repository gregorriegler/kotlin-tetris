import kotlin.math.roundToInt

class Frame(
    val width: Int,
    val height: Int
) {
    fun drawEmpty(): List<List<String>> {
        return (0 until height)
            .map { (0 until width).map { "_" }.toList() }
            .toList()
    }

    fun center() = width.toDouble().div(2).roundToInt() - 1
}