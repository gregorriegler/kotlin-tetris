package com.gregorriegler.tetris.model

data class Score(val area: Area, val score: Int) {
    constructor(area: String, score: Int) : this(Area(area), score)

    fun plus(score: Int): Score = Score(area, this.score + score)
}