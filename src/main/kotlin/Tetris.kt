class Tetris(
    private val width: Int,
    private val height: Int
) {
    private val frame: Frame = Frame(width, height)
    private var score: Int = 0
    private var stone: Stone = Stone(frame)
    private val clock: GameClock = GameClock({ tick() })

    private var falling: List<List<String>> = frame.drawEmpty()
    private var landed: List<List<String>> = frame.drawEmpty()

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
            stone.x = frame.center()
            if (bottomLineFilled()) {
                dissolveLine()
            } else {
                startNextStone()
                return // stays, no falling!
            }
        } else {
            stone.down()
            falling = stone.render()
        }
    }

    fun left() {
        if (stone.x > 0) {
            stone.left()
            falling = stone.render()
        }
    }

    fun right() {
        if (stone.x + 1 <= frame.width - 1) {
            stone.right()
            falling = stone.render()
        }
    }

    private fun gameOver(): Boolean {
        return isStone(landed[0][stone.x])
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stone.down()
        falling = stone.render()
        startNextStone()
    }

    private fun startNextStone() {
        stone = Stone(frame)
    }

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom(y: Int) = y == bottom() || isStone(landed[y + 1][stone.x])

    private fun land() {
        landed = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (isStone(falling[rowIndex][stone.x])) {
                mutableRow[stone.x] = falling[rowIndex][stone.x]
            }

            mutableRow
        }
    }

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