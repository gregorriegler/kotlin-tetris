package com.gregorriegler.tetris.model

interface Frame {
    val width: Int
    val height: Int

    fun rows() = (0 until height)
    fun columns() = (0 until width)
}

