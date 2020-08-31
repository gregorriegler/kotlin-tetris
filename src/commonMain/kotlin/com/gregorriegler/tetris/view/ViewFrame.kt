package com.gregorriegler.tetris.view

import com.gregorriegler.tetris.model.Frame

class ViewFrame(
    val left: Int,
    val top: Int,
    override val width: Int,
    override val height: Int,
) : Frame {

    companion object {
        fun max(tetris: Frame, available: Frame): ViewFrame {
            val width = (available.height / tetris.height) * tetris.width
            return ViewFrame(
                available.width / 2 - width / 2,
                0,
                width,
                available.height
            )
        }
    }

    val right: Int
        get() = left + width
    val bottom: Int
        get() = top + height

    override fun toString(): String {
        return "ViewFrame(left=$left, top=$top, right=$right, bottom=$bottom)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ViewFrame

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