class Tetris(width: Int, height: Int) {
    private val frame: Frame = Frame(width, height)
    private var stone: Stone = Stone(frame)
    private var score: Int = 0
    private val clock: GameClock = GameClock({ tick() })
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
                stone.down()
            }
            stone = Stone(frame)
            return
        } else {
            stone.down()
        }
    }

    fun left() {
        stone.left()
    }

    fun right() {
        stone.right()
    }

    private fun dissolveLine() {
        debris = listOf((0 until frame.width)
            .map { Field.EMPTY }.toList()) + debris.dropLast(1)
            .toMutableList()
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

    private fun isStone(field: String) = field != Field.EMPTY

    fun display(): String {
        if (gameOver()) {
            return """
            ##
            Game Over
            ##
            """.trimIndent()
        }

        val stoneRender = stone.render()
        val combined = debris.mapIndexed { rowIndex, landedRow ->
            landedRow.mapIndexed { columnIndex, column ->
                if (isStone(column) || isStone(stoneRender[rowIndex][columnIndex]))
                    "#"
                else
                    Field.EMPTY
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