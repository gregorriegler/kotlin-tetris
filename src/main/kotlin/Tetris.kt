import java.util.Collections.unmodifiableList

class Tetris(
    private val width: Int,
    height: Int
) {
    private val frame: Frame = Frame(width, height)
    private var score: Int = 0
    private var stone: Stone = Stone(frame)
    private val clock: GameClock = GameClock({ tick() })

    private var falling: List<List<String>> = frame.drawEmpty()
    private var debris: List<List<String>> = frame.drawEmpty()

    fun time(time: Long) {
        clock.time(time)
    }

    private fun tick() {
        if (gameOver()) {
            return
        }

        if (stone.landed(debris)) {
            debris = stone.addToDebris(debris)
            if (bottomLineFilled()) {
                increaseScore()
                dissolveLine()
                falling = stone.down()
            }
            stone = Stone(frame)
            return
        } else {
            falling = stone.down()
        }
    }

    fun left() {
        falling = stone.left()
    }

    fun right() {
        falling = stone.right()
    }

    private fun dissolveLine() {
        debris = listOf((0 until width).map { "_" }.toList()) + debris.dropLast(1).toMutableList()
    }

    private fun gameOver(): Boolean {
        return isStone(debris[0][stone.x])
    }

    private fun increaseScore() {
        score += 1
    }

    private fun bottomLineFilled(): Boolean {
        return debris.last().all { isStone(it) }
    }

    private fun isStone(field: String) = field != "_"

    fun display(): String {
        if (gameOver()) {
            return """
            ##
            Game Over
            ##
            """.trimIndent()
        }

        val combined = debris.mapIndexed { rowIndex, landedRow ->
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