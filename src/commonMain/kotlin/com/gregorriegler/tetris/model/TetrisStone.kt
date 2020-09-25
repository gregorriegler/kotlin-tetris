package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class TetrisStone(x: Int, y: Int, width: Int, height: Int, val color: Color) :
    SimplePositionedFrame(x, y, width, height) {

    companion object {
        fun of(
            display: Frame,
            gameSize: Frame,
            field: Field,
            depth: Int,
            palette: List<Color>
        ): TetrisStone {
            val stoneWidth = display.width / gameSize.width
            val stoneHeight = display.height / gameSize.height
            val squareLeft = field.x * stoneWidth
            val squareTop = field.y * stoneHeight

            return TetrisStone(
                squareLeft,
                squareTop,
                stoneWidth,
                stoneHeight,
                field.color(depth, palette)
            )
        }
    }

}