class Tetris(width: Int, height: Int) {
    private val frame: Frame = Frame(width, height)
    private var stone: Stone = Stone(frame)
    private var score: Int = 0
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
            if (debris.bottomLineFilled()) {
                debris.dissolveLine()
                increaseScore()
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

    private fun gameOver(): Boolean {
        return debris.hasDebris(frame.topCenter())
    }

    private fun increaseScore() {
        score += 1
    }

    fun display(): String {
        if (gameOver()) {
            return """
            ##
            Game Over
            ##
            """.trimIndent()
        }

        val combined = debris.drawWithStone(stone)
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