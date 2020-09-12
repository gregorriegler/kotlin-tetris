package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class Tetris(
    private val frame: TetrisFrame,
    private val stones: List<Structure>,
    private val digAmount: Int,
) : Frame {
    private var stone: Stone = Stone(stones.random(), this.frame)
    private val clock: Timer = Timer({ tick() })
    private val debris: Debris = Debris(this.frame)

    override val width get() = frame.width
    override val height get() = frame.height

    constructor() : this(
        TetrisFrame(12, 24),
        listOf(
//            Structure.createDot(),
//            Structure.createI(),
//            Structure.createT(),
//            Structure.createL(),
//            Structure.createJ(),
            Structure.createBomb()
        ),
        4
    )

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
            debris.dig(digAmount)
            stone.down()
        }

        tick++
    }

    fun gameDisplayString(): String = debris.withStone(stone).toString()
    fun gameDisplay(): Debris = debris.withStone(stone)

    private fun increaseScore(count: Int) {
        score += count
    }

    private fun gameIsOver(): Boolean = frame.topCenterFilled().collidesWith(debris.area)
}