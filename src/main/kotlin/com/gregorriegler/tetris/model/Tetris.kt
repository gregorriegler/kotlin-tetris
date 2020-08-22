package com.gregorriegler.tetris.model

class Tetris(width: Int, height: Int, private val stones: List<Structure>) {
    private val frame: Frame = Frame(width, height)
    private var stone: Stone = Stone(stones.random(), frame)
    private val clock: Timer = Timer({ tick() })
    private val debris: Debris = Debris(frame)

    var nextStone: Structure = stones.random()
        private set
    var score: Int = 0
        private set
    var gameOver: String = ""
        private set

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

        if (stone.landed(debris)) {
            debris.add(stone)
            increaseScore(debris.dissolveFilledRows())
            stone = Stone(nextStone, frame)
            nextStone = stones.random()
            if(gameIsOver()) {
                gameOver = "Game Over"
            }
            return
        } else {
            stone.down()
        }
    }

    fun gameDisplay(): String = debris.withStone(stone).toString()

    private fun increaseScore(count: Int) {
        score += count
    }

    private fun gameIsOver(): Boolean = debris.collidesWith(frame.topCenterFilled())
}