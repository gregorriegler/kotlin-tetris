package com.gregorriegler.tetris.model

interface Collidable {
    fun collidesWith(field: Field): Boolean
}