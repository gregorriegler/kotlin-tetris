package com.gregorriegler.tetris.model

interface PositionedFrame : Frame {
    val x: Int
    val y: Int
    override val width: Int
    override val height: Int
    val rightSide: Int
    val bottom: Int
}