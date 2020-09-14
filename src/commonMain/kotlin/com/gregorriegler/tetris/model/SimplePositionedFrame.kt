package com.gregorriegler.tetris.model

open class SimplePositionedFrame(
    final override val x: Int,
    final override val y: Int,
    final override val width: Int,
    final override val height: Int,
) : PositionedFrame {

    override val rightSide: Int = x + width
    override val bottom: Int = y + height

    companion object {
        fun max(subject: Frame, availableSpace: Frame): PositionedFrame {
            val width = (availableSpace.height / subject.height) * subject.width
            return SimplePositionedFrame(
                availableSpace.width / 2 - width / 2,
                0,
                width,
                availableSpace.height
            )
        }
    }


    override fun toString(): String {
        return "SimplePositionedFrame(left=$x, top=$y, right=$rightSide, bottom=$bottom)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SimplePositionedFrame

        if (x != other.x) return false
        if (y != other.y) return false
        if (rightSide != other.rightSide) return false
        if (bottom != other.bottom) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + rightSide
        result = 31 * result + bottom
        return result
    }


}