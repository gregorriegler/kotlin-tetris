class Tetris(width: Int, height: Int) {
    private var score: Int = 0
    private var position: Int = -1
    private var field = (0 until height)
        .map { (0 until width).map { "_" }.toList() }
        .toList()

    fun tick() {
        if (canDissolve()) {
            dissolveLine()
        } else {
            stoneFalls()
        }
    }

    private fun dissolveLine() {
        increaseScore()
        stoneFalls()
        position = -1
    }

    private fun stoneFalls() {
        position++
        field = field.mapIndexed { index, row ->
            val mutableList = (0 until row.size).map { "_" }.toMutableList()
            if (hasStone(index)) {
                mutableList.set(0,"#")
            }
            mutableList
        }
    }

    private fun hasStone(index: Int) = index == position

    private fun increaseScore() {
        score += 1
    }

    private fun canDissolve() = position == (bottom())

    private fun bottom() = field.size - 1

    fun display(): String {
        return field.joinToString(separator = "\n") { it.joinToString(separator = "") { it } }
    }

    fun score(): Int {
        return score
    }
}