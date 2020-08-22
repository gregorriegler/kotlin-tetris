class Tetris(width: Int, height: Int, private val stones: List<Structure>) {
    private val frame: Frame = Frame(width, height)
    private var stone: Stone = Stone(stones.random(), frame)
    private val clock: GameClock = GameClock({ tick() })
    private val debris: Debris = Debris(frame)
    var score: Int = 0
        private set

    fun left() = stone.left(debris)
    fun right() = stone.right(debris)
    fun rotate() = stone.rotate()
    fun speed() = clock.speed()
    fun normal() = clock.normal()
    fun time(time: Long) {
        clock.time(time)
    }

    private fun tick() {
        if (gameOver()) {
            return
        }

        if (stone.landed(debris)) {
            debris.add(stone)
            increaseScore(debris.dissolveFilledRows())
            stone = Stone(stones.random(), frame)
            return
        } else {
            stone.down()
        }
    }

    fun display(): String =
        if (gameOver()) {
            """
            ##
            Game Over
            ##
            """.trimIndent()
        } else {
            Area.draw(debris.stateWithStone(stone))
        }

    private fun increaseScore(count: Int) {
        score += count
    }

    private fun gameOver(): Boolean = debris.collidesWith(frame.topCenterFilled())
}