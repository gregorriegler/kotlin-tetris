package com.gregorriegler.tetris.model

interface Frame {
    val width: Int
    val height: Int
}

class SimpleFrame (
    override val width: Int,
    override val height: Int
) : Frame