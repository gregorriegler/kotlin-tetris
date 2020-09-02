package com.gregorriegler.tetris.view

import com.gregorriegler.tetris.model.Frame

class PositionedFrame(
    val left: Int,
    val top: Int,
    override val width: Int,
    override val height: Int,
) : Frame {

    val right: Int = left + width
    val bottom: Int = top + height

    companion object {
        fun max(subject: Frame, availableSpace: Frame): PositionedFrame {
            val width = (availableSpace.height / subject.height) * subject.width
            return PositionedFrame(
                availableSpace.width / 2 - width / 2,
                0,
                width,
                availableSpace.height
            )
        }
    }

    override fun toString(): String {
        return "ViewFrame(left=$left, top=$top, right=$right, bottom=$bottom)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PositionedFrame

        if (left != other.left) return false
        if (top != other.top) return false
        if (right != other.right) return false
        if (bottom != other.bottom) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left
        result = 31 * result + top
        result = 31 * result + right
        result = 31 * result + bottom
        return result
    }


}