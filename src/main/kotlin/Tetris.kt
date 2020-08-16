class Tetris(width: Int, height: Int) {
    private var score: Int = 0
    private var position: Int = -1
    private val width: Int = width;

    private var falling = (0 until height)
        .map { (0 until width).map { "_" }.toList() }
        .toList()
    private var landed: List<List<String>> = (0 until height)
        .map { (0 until width).map { "_" }.toList() }
        .toList()

    fun tick() {
        if (arrivedAtBottom()) {
            land()
            if (bottomLineFilled()) {
                dissolveLine()
            } else {
                position = -1
                return // stays, no falling!
            }
        } else {
            stoneFalls()
        }
    }

    private fun dissolveLine() {
        increaseScore()
        landed = listOf((0 until width).map { "_" }.toList()) + landed.dropLast(1).toMutableList()
        stoneFalls()
        position = -1
    }

    private fun stoneFalls() {
        position++
        falling = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (hasStone(rowIndex)) {
                mutableRow.set(0, "#")
            }
            mutableRow
        }
    }

    private fun hasStone(index: Int) = index == position

    private fun increaseScore() {
        score += 1
    }

    private fun arrivedAtBottom() = position == (bottom()) || landed.get(position + 1).get(0) != "_"

    private fun land() {
        landed = landed.mapIndexed { rowIndex, row ->
            val mutableRow = row.toMutableList()

            if (falling.get(rowIndex).get(0) != "_") {
                mutableRow.set(0, falling.get(rowIndex).get(0))
            }

            mutableRow
        }
    }

    private fun bottomLineFilled(): Boolean {
        return falling.last().all { it != "_" }
    }

    private fun bottom() = falling.size - 1

    fun display(): String {
        val combined = landed.mapIndexed { rowIndex, landedRow ->
            landedRow.mapIndexed { columnIndex, column ->
                if (column != "_" || falling.get(rowIndex).get(columnIndex) != "_")
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