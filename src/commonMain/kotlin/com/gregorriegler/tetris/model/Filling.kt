package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.view.Color

enum class Filling {

    BOMB {
        override fun combine(filling: Filling): Filling = BOMB
        override fun falls(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.black
        override fun special(position: Position, area: Area): Area = explode(position, area)
        override fun toString(): String = BOMB_VALUE.toString()

        private fun explode(position: Position, area: Area): Area = area.erase(Area.circle(position, 4))
    },
    SOIL {
        override fun combine(filling: Filling): Filling = SOIL
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.byDepth(soilColors, depth, Color.changeColorEvery)
        override fun toString(): String = SOIL_VALUE.toString()
    },
    COIN {
        override fun combine(filling: Filling): Filling = this
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.gold
        override fun toString(): String = COIN_VALUE.toString()
    },
    FALLING {
        override fun combine(filling: Filling): Filling = FALLING
        override fun falls(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(depth: Int, soilColors: List<Color>): Color = Color.orange
        override fun toString(): String = FILLED_VALUE.toString()
    },
    EMPTY {
        override fun combine(filling: Filling): Filling = filling
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun toString(): String = EMPTY_VALUE.toString()
    },
    INDENT {
        override fun combine(filling: Filling): Filling = filling
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun toString(): String = INDENT_VALUE.toString()
    };


    abstract fun combine(filling: Filling): Filling
    abstract fun falls(): Boolean
    abstract fun isEmpty(): Boolean
    abstract fun collides(): Boolean
    open fun color(depth: Int, soilColors: List<Color>) = Color.grey
    open fun special(position: Position, area: Area): Area = area


    companion object {
        const val FILLED_VALUE: Char = '#'
        const val SOIL_VALUE: Char = '■'
        const val EMPTY_VALUE: Char = '-'
        const val BOMB_VALUE: Char = 'X'
        const val COIN_VALUE: Char = 'O'
        const val INDENT_VALUE: Char = '>'
        const val PULL_VALUE: Char = '<'

        fun of(char: Char): Filling =
            when (char) {
                FILLED_VALUE -> FALLING
                SOIL_VALUE -> SOIL
                INDENT_VALUE -> INDENT
                BOMB_VALUE -> BOMB
                COIN_VALUE -> COIN
                EMPTY_VALUE -> EMPTY
                else -> EMPTY
            }

        fun higher(filling1: Filling, filling2: Filling): Filling {
            return if (filling1.ordinal < filling2.ordinal) filling1 else filling2
        }

        fun soilOrCoin(coinPercentage: Int): Filling =
            when (coinPercentage) {
                0 -> SOIL
                100 -> COIN
                else -> random(coinPercentage)
            }

        private fun random(coinPercentage: Int): Filling {
            return ((0 until coinPercentage).map { COIN } + (coinPercentage until 100).map { SOIL }).random()
        }
    }
}