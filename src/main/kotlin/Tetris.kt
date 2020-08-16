import java.util.*

class Tetris(private val width: Int, private val height: Int) {
    private var score: Int = 0
    private var position: Int = -1
    private var x: Int = calcCenter()

    private var falling: List<List<String>> = createEmptyBoard(width, height)
    private var landed: List<List<String>> = createEmptyBoard(width, height)

    private fun createEmptyBoard(width: Int, height: Int): List<List<String>> {
        return (0 until height)
            .map { (0 until width).map { "_" }.toList() }
            .toList()
    }

    fun tick() {
        if (gameOver()) {
            return
        }

        if (arrivedAtBottom(position)) {
            land()
            x = calcCenter()
            if (bottomLineFilled()) {
                dissolveLine()
            } else {
                startNextStone()
                return // stays, no falling!
            }
        } else {
            stoneFalls(position)
        }
    }

    fun left() {
        if (x > 0) {
            x--
            falling = calcFallingStone()
        }
    }

    fun right() {
        if (x + 1 <= width - 1) {
            x++
            falling = calcFallingStone()
        }
    }

    private fun gameOver(): Boolean {
        return isStone(landed.get(0).get(x()))
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stoneFalls(position)
        startNextStone()
    }

    private fun stoneFalls(i: Int) {
        stoneDown(i)
        falling = calcFallingStone()
    }

    private fun calcFallingStone(): List<List<String>> {
        return createEmptyBoard(width, height).mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (hasStone(rowIndex)) {
                mutableRow.set(x(), "#")
            }

            Collections.unmodifiableList(mutableRow)
        }
    }

    private fun stoneDown(position: Int) {
        this.position = position + 1
    }

    private fun startNextStone() {
        position = -1
    }

    private fun hasStone(index: Int) = index == position

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom(position: Int) = position == (bottom()) || isStone(landed.get(position + 1).get(x()))

    private fun land() {
        landed = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (isStone(falling.get(rowIndex).get(x()))) {
                mutableRow.set(x(), falling.get(rowIndex).get(x()))
            }

            mutableRow
        }
    }

    private fun x() = x

    private fun calcCenter() = Math.round(width.toDouble().div(2)).toInt() - 1

    private fun bottomLineFilled(): Boolean {
        return landed.last().all { isStone(it) }
    }

    private fun isStone(field: String) = field != "_"

    private fun bottom() = falling.size - 1

    fun display(): String {
        if (gameOver()) {
            return """
            ##
            Game Over
            ##
            """.trimIndent()
        }

        val combined = landed.mapIndexed { rowIndex, landedRow ->
            landedRow.mapIndexed { columnIndex, column ->
                if (isStone(column) || isStone(falling.get(rowIndex).get(columnIndex)))
                    "#"
                else
                    "_"
            }
        }

        return combined.joinToString(separator = "\n") { it.joinToString(separator = "") { it } }
    }

    fun score(): Int {
        return score
    }
}