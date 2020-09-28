package com.gregorriegler.tetris.model

data class EraseResult(val area: Area, val score: Int) {
    constructor(area: String, score: Int) : this(Area(area), score)

    fun plus(score: Int): EraseResult = EraseResult(area, this.score + score)
}