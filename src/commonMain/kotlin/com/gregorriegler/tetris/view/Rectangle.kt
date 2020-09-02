package com.gregorriegler.tetris.view

import com.gregorriegler.tetris.model.Area
import com.gregorriegler.tetris.model.Field

class Rectangle(
    val left: Int,
    val top: Int,
    val width: Int,
    val height: Int,
) {
    val right: Int = left + width
    val bottom: Int = top + height

    companion object {
        fun fromArea(frame: PositionedFrame, area: Area, field: Field): Rectangle {
            val stoneWidth = frame.width / area.width
            val stoneHeight = frame.height / area.height
            val squareLeft = frame.left + field.x * stoneWidth + 4
            val squareTop = frame.top + field.y * stoneHeight + 4

            return Rectangle(
                squareLeft,
                squareTop,
                stoneWidth,
                stoneHeight
            )
        }
    }
}