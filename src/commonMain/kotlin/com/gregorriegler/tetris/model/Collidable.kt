package com.gregorriegler.tetris.model

interface Collidable {
    fun collidingAt(): List<Position>
    fun collidesWith(other: Collidable): Boolean = collidingAt().any { other.collidingAt().contains(it) }

}