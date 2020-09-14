package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class TetrisStone(x: Int, y: Int, width: Int, height: Int, val color: Color) :
    SimplePositionedFrame(x, y, width, height) {

    companion object {
        fun of(
            display: PositionedFrame,
            gameSize: Frame,
            field: Field,
            depth: Int,
            palette: List<Color>
        ): TetrisStone {
            val stoneWidth = display.width / gameSize.width
            val stoneHeight = display.height / gameSize.height
            val squareLeft = display.x + field.x * stoneWidth + 4
            val squareTop = display.y + field.y * stoneHeight + 4

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