package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

enum class Filling {

    BOMB {
        override fun combine(filling: Filling): Filling = BOMB
        override fun isFilled(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.black

        override fun special(position: MovablePosition, area: Area): Area = explode(position, area)
        override fun toString(): String = BOMB_VALUE.toString()

        private fun explode(position: MovablePosition, area: Area): Area = area.erase(Area.circle(position, 4))
    },
    SOIL {
        override fun combine(filling: Filling): Filling = SOIL
        override fun isFilled(): Boolean = false
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color =
            Color.byDepth(soilColors, depth, Color.changeColorEvery)

        override fun toString(): String = SOIL_VALUE.toString()
    },
    FILLED {
        override fun combine(filling: Filling): Filling = FILLED
        override fun isFilled(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.orange

        override fun toString(): String = FILLED_VALUE.toString()
    },
    EMPTY {
        override fun combine(filling: Filling): Filling = filling
        override fun isFilled(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun toString(): String = EMPTY_VALUE.toString()
    },
    INDENT {
        override fun combine(filling: Filling): Filling = filling
        override fun isFilled(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun toString(): String = INDENT_VALUE.toString()
    };

    abstract fun combine(filling: Filling): Filling
    abstract fun isFilled(): Boolean
    abstract fun isEmpty(): Boolean
    abstract fun collides(): Boolean
    open fun color(depth: Int, soilColors: List<Color>) = Color.grey
    open fun special(position: MovablePosition, area: Area): Area = area


    companion object {
        const val FILLED_VALUE: Char = '#'
        const val SOIL_VALUE: Char = '■'
        const val EMPTY_VALUE: Char = '-'
        const val BOMB_VALUE: Char = 'X'
        const val INDENT_VALUE: Char = '>'
        const val PULL_VALUE: Char = '<'

        fun of(char: Char): Filling =
            when (char) {
                FILLED_VALUE -> FILLED
                SOIL_VALUE -> SOIL
                EMPTY_VALUE -> EMPTY
                INDENT_VALUE -> INDENT
                BOMB_VALUE -> BOMB
                else -> EMPTY
            }

        fun higher(filling1: Filling, filling2: Filling): Filling {
            return if (filling1.ordinal < filling2.ordinal) filling1 else filling2
        }
    }
}