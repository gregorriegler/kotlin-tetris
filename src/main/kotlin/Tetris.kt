class Tetris(height: Int) {
    private var score: Int = 0
    private var position: Int = 0
    private var field = (position until height)
        .map { "_" }
        .toList()

    fun drop() {
        position = 0
        putStoneAt(position)
    }

    fun tick() {
        if(canDissolve()) {
            increaseScore()
        }
        position += 1
        putStoneAt(position)
    }

    private fun increaseScore() {
        score += 1
    }

    private fun canDissolve() = position == (field.size - 1)

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