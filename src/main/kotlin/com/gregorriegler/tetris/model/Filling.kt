package com.gregorriegler.tetris.model

enum class Filling {

    FILLED {
        override fun or(filling: Filling): Filling = FILLED
        override fun toString(): String = FILLED_VALUE
    },
    EMPTY {
        override fun or(filling: Filling): Filling = filling
        override fun toString(): String = EMPTY_VALUE
    },
    INDENT {
        override fun or(filling: Filling): Filling = filling
        override fun toString(): String = INDENT_VALUE
    };

    abstract fun or(filling: Filling): Filling

    companion object {
        const val FILLED_VALUE: String = "#"
        const val EMPTY_VALUE: String = "-"
        const val INDENT_VALUE: String = ">"

        fun of(string: String): Filling =
            when (string) {
                FILLED_VALUE -> FILLED
                EMPTY_VALUE -> EMPTY
                INDENT_VALUE -> INDENT
                else -> EMPTY
            }
    }
}