class Tetris(height: Int) {
    private var score: Int = 0
    private var position: Int = -1
    private var field = (position + 1 until height)
        .map { "_" }
        .toList()

    fun tick() {
        if (canDissolve()) {
            increaseScore()
        }

        position++
        putStoneAt(position)
    }

    private fun increaseScore() {
        score += 1
    }

    private fun canDissolve() = position == (bottom())

    private fun bottom() = field.size - 1

    fun display(): String {
        return field.joinToString(separator = "\n") { it }
    }

    private fun putStoneAt(position: Int) {
        field = field.mapIndexed { index, _ -> if (index == position) "#" else "_" }
    }

    fun score(): Int {
        return score
    }
}