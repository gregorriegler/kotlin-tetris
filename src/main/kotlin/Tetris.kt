import kotlin.math.roundToInt

class Tetris(
    private val width: Int,
    private val height: Int
) {
    private var score: Int = 0
    private var x: Int = calcCenter()
    private var stone: Stone = Stone(calcCenter())
    private val clock: GameClock = GameClock({ tick() })

    private var falling: List<List<String>> = createEmptyBoard(width, height)
    private var landed: List<List<String>> = createEmptyBoard(width, height)

    private fun createEmptyBoard(width: Int, height: Int): List<List<String>> {
        return (0 until height)
            .map { (0 until width).map { "_" }.toList() }
            .toList()
    }

    fun time(time: Long) {
        clock.time(time)
    }

    private fun tick() {
        if (gameOver()) {
            return
        }

        if (arrivedAtBottom(stone.y)) {
            land()
            x = calcCenter()
            if (bottomLineFilled()) {
                dissolveLine()
            } else {
                startNextStone()
                return // stays, no falling!
            }
        } else {
            stoneFalls()
        }
    }

    fun left() {
        if (x > 0) {
            x--
            stone.left()
            falling = renderFallingStone(createEmptyBoard(width, height))
        }
    }

    fun right() {
        if (x + 1 <= width - 1) {
            x++
            stone.right()
            falling = renderFallingStone(createEmptyBoard(width, height))
        }
    }

    private fun gameOver(): Boolean {
        return isStone(landed[0][x])
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stoneFalls()
        startNextStone()
    }

    private fun stoneFalls() {
        stone.down()
        falling = renderFallingStone(createEmptyBoard(width, height))
    }

    private fun renderFallingStone(emptyBoard: List<List<String>>): List<List<String>> {
         return stone.render(emptyBoard)
    }

    private fun startNextStone() {
        stone = Stone(calcCenter())
    }

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom(y: Int) = y == bottom() || isStone(landed[y + 1][x])

    private fun land() {
        landed = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (isStone(falling[rowIndex][x])) {
                mutableRow[x] = falling[rowIndex][x]
            }

            mutableRow
        }
    }

    private fun calcCenter() = width.toDouble().div(2).roundToInt() - 1

    private fun bottomLineFilled(): Boolean {
        return landed.last().all { isStone(it) }
    }

    private fun isStone(field: String) = field != "_"

    private fun bottom() = height - 1

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
                if (isStone(column) || isStone(falling[rowIndex][columnIndex]))
                    "#"
                else
                    "_"
            }
        }

        return combined.joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it } }
    }

    fun score(): Int {
        return score
    }

    fun speed() {
        clock.speed()
    }

    fun normal() {
        clock.normal()
    }
}