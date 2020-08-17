import java.util.*
import kotlin.math.roundToInt

class Tetris(private val width: Int, private val height: Int) {
    private var score: Int = 0
    private var x: Int = calcCenter()
    private var y: Int = -1
    private val clock: GameClock = GameClock({tick()})

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

        if (arrivedAtBottom(y)) {
            land()
            x = calcCenter()
            if (bottomLineFilled()) {
                dissolveLine()
            } else {
                startNextStone()
                return // stays, no falling!
            }
        } else {
            stoneFalls(y)
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
        return isStone(landed[0][x])
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stoneFalls(y)
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
                mutableRow[x] = "#"
            }

            Collections.unmodifiableList(mutableRow)
        }
    }

    private fun stoneDown(position: Int) {
        this.y = position + 1
    }

    private fun startNextStone() {
        y = -1
    }

    private fun hasStone(index: Int) = index == y

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom(position: Int) = position == (bottom()) || isStone(landed[position + 1][x])

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