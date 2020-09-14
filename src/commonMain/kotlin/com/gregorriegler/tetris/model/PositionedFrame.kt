package com.gregorriegler.tetris.model

interface PositionedFrame : Frame, Position {
    override val x: Int
    override val y: Int
    override val width: Int
    override val height: Int
    val rightSide: Int
    val bottom: Int
}