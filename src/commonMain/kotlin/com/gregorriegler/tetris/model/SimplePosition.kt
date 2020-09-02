package com.gregorriegler.tetris.model

class SimplePosition(override val x: Int, override val y: Int) : Position {
    fun up(by: Int): SimplePosition = SimplePosition(this.x, this.y - by)
    fun down(): SimplePosition = SimplePosition(this.x, this.y + 1)
    fun down(by: Int): SimplePosition = SimplePosition(this.x, this.y + by)
    fun left(): SimplePosition = left(1)
    fun left(by: Int): SimplePosition = SimplePosition(this.x - by, this.y)
    fun right(): SimplePosition = right(1)
    fun right(by: Int): SimplePosition = SimplePosition(this.x + by, this.y)
    fun rotate(width: Int): SimplePosition = SimplePosition(width - this.y - 1, this.x)
    fun plus(position: Position): SimplePosition = SimplePosition(this.x + position.x, this.y + position.y)
    fun minus(position: Position): SimplePosition = SimplePosition(this.x - position.x, this.y - position.y)
}