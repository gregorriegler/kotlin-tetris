class Tetris(width: Int, height: Int, private val stones: List<Structure>) {
    companion object {
        fun draw(combined: List<List<Filling>>) =
            combined.joinToString(separator = "\n") { it -> it.joinToString(separator = "") { it.toString() } }
    }

    private val frame: Frame = Frame(width, height)
    private var stone: Stone = Stone(stones.random(), frame)
    var score: Int = 0
        private set

    private val clock: GameClock = GameClock({ tick() })
    private val debris: Debris = Debris(frame)

    fun time(time: Long) {
        clock.time(time)
    }

    private fun tick() {
        if (gameOver()) {
            return
        }

        if (stone.landed(debris)) {
            debris.add(stone)
            val count = debris.dissolveFilledRows()
            increaseScore(count)
            stone = Stone(stones.random(), frame)
            return
        } else {
            stone.down()
        }
    }

    fun left() {
        stone.left(debris)
    }

    fun right() {
        stone.right(debris)
    }

    fun rotate() {
        stone.rotate()
    }

    private fun gameOver(): Boolean {
        return debris.isAt(frame.topCenter())
    }

    private fun increaseScore(count: Int) {
        score += count
    }

    fun display(): String {
        if (gameOver()) {
            return """
            ##
            Game Over
            ##
            """.trimIndent()
        }

        return draw(debris.stateWithStone(stone))
    }

    fun speed() {
        clock.speed()
    }

    fun normal() {
        clock.normal()
    }
}