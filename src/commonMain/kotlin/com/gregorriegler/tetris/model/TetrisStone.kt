package com.gregorriegler.tetris.model

class TetrisStone(x: Int, y: Int, width: Int, height: Int, val color: Color) :
    SimplePositionedFrame(x, y, width, height) {

    companion object {
        fun of(
            display: Frame,
            gameSize: Frame,
            field: Field,
            depth: Int,
            palette: Palette
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
                field.color(palette, depth)
            )
        }
    }

}