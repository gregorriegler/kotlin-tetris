package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

class ColoredPositionedFrame(x: Int, y: Int, width: Int, height: Int, val color: Color) :
    SimplePositionedFrame(x, y, width, height) {

    companion object {
        fun tetrisStone(display: PositionedFrame, gameSize: Frame, stone: Position, color: Color): ColoredPositionedFrame {
            val stoneWidth = display.width / gameSize.width
            val stoneHeight = display.height / gameSize.height
            val squareLeft = display.x + stone.x * stoneWidth + 4
            val squareTop = display.y + stone.y * stoneHeight + 4

            return ColoredPositionedFrame(
                squareLeft,
                squareTop,
                stoneWidth,
                stoneHeight,
                color
            )
        }
    }

}