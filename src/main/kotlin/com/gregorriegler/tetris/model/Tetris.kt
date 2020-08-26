package com.gregorriegler.tetris.model

class Tetris(private val frame: Frame, private val stones: List<Structure>) {
    private var stone: Stone = Stone(stones.random(), this.frame)
    private val clock: Timer = Timer({ tick() })
    private val debris: Debris = Debris(this.frame)

    var nextStone: Structure = stones.random()
        private set
    var score: Int = 0
        private set
    var gameOver: String = ""
        private set
    var tick: Int = 1

    fun left() = stone.left(debris)
    fun right() = stone.right(debris)
    fun rotate() = stone.rotate(debris)
    fun speed() = clock.speed()
    fun normal() = clock.normal()
    fun time(time: Long) = clock.time(time)

    private fun tick() {
        if (gameIsOver()) {
            return
        }

        if (tick % 64 == 0) {
            debris.dig(1)
        }

        if (stone.landed(debris)) {
            debris.add(stone)
            debris.specials()
            increaseScore(debris.eraseFilledRows())
            stone = Stone(nextStone, frame)
            nextStone = stones.random()
            if (gameIsOver()) {
                gameOver = "Game Over"
            }
            return
        } else {
            debris.fall()
            stone.down()
        }

        tick++
    }

    fun gameDisplay(): String = debris.withStone(stone).toString()

    private fun increaseScore(count: Int) {
        score += count
    }

    private fun gameIsOver(): Boolean = debris.collidesWith(frame.topCenterFilled())
}