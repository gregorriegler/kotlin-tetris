package com.gregorriegler.tetris.model

class SimpleFrame (
    override val width: Int,
    override val height: Int
) : Frame {

    override fun toString(): String {
        return "SimpleFrame(width=$width, height=$height)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SimpleFrame

        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}

