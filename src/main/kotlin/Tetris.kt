class Tetris(private val width: Int, height: Int) {
    private var score: Int = 0
    private var position: Int = -1

    private var falling: List<List<String>> = (0 until height)
        .map { (0 until width).map { "_" }.toList() }
        .toList()
    private var landed: List<List<String>> = (0 until height)
        .map { (0 until width).map { "_" }.toList() }
        .toList()

    fun tick() {
        if (gameOver()) {
            return
        }

        if (arrivedAtBottom(position)) {
            land()
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

    private fun gameOver(): Boolean {
        return isStone(landed.get(0).get(center()))
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stoneFalls()
        startNextStone()
    }

    private fun stoneFalls() {
        stoneDown(position)
        falling = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (hasStone(rowIndex)) {
                mutableRow.set(center(), "#")
            }
            mutableRow
        }
    }

    private fun startNextStone() {
        position = -1
    }

    private fun stoneDown(position: Int) {
        this.position = position + 1
    }

    private fun hasStone(index: Int) = index == position

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom(position: Int) = position == (bottom()) || isStone(landed.get(position + 1).get(center()))

    private fun land() {
        landed = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (isStone(falling.get(rowIndex).get(center()))) {
                mutableRow.set(center(), falling.get(rowIndex).get(center()))
            }

            mutableRow
        }
    }

    private fun center() = Math.round(width.toDouble().div(2)).toInt() - 1

    private fun bottomLineFilled(): Boolean {
        return falling.last().all { isStone(it) }
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