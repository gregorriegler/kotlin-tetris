class Tetris(height: Int) {
    private var score: Int = 0
    private var position: Int = -1
    private var field = (position + 1 until height)
        .map { "_" }
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
        field = field.mapIndexed { index, _ -> if (hasStone(index)) "#" else "_" }
    }

    private fun hasStone(index: Int) = index == position

    private fun increaseScore() {
        score += 1
    }

    private fun canDissolve() = position == (bottom())

    private fun bottom() = field.size - 1

    fun display(): String {
        return field.joinToString(separator = "\n") { it }
    }

    fun score(): Int {
        return score
    }
}