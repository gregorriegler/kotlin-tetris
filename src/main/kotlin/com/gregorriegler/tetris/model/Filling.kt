package com.gregorriegler.tetris.model

enum class Filling {

    FILLED {
        override fun or(filling: Filling): Filling = FILLED
        override fun isFilled(): Boolean = true
        override fun toString(): String = FILLED_VALUE.toString()
    },
    BOMB {
        override fun or(filling: Filling): Filling = BOMB
        override fun isFilled(): Boolean = true
        override fun toString(): String = BOMB_VALUE.toString()
    },
    EMPTY {
        override fun or(filling: Filling): Filling = filling
        override fun isFilled(): Boolean = false
        override fun toString(): String = EMPTY_VALUE.toString()
    },
    INDENT {
        override fun or(filling: Filling): Filling = filling
        override fun isFilled(): Boolean = false

        override fun toString(): String = INDENT_VALUE.toString()
    };

    abstract fun or(filling: Filling): Filling
    abstract fun isFilled(): Boolean

    companion object {
        const val FILLED_VALUE: Char = '#'
        const val EMPTY_VALUE: Char = '-'
        const val BOMB_VALUE: Char = 'X'
        const val INDENT_VALUE: Char = '>'
        const val PULL_VALUE: Char = '<'

        fun of(char: Char): Filling =
            when (char) {
                FILLED_VALUE -> FILLED
                EMPTY_VALUE -> EMPTY
                INDENT_VALUE -> INDENT
                BOMB_VALUE -> BOMB
                else -> EMPTY
            }
    }
}