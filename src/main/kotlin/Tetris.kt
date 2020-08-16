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
        if(position == (field.size - 1)) {
            score += 1
        }
        position += 1
        putStoneAt(position)
    }

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