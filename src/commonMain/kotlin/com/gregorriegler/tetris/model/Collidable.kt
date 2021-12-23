package com.gregorriegler.tetris.model

interface Collidable {
    fun collidingPositions(): List<Position>
    fun collidesWith(other: Collidable): Boolean =
        collidingPositions().any { other.collidingPositions().contains(it) }

}