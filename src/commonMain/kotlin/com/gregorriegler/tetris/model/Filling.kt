package com.gregorriegler.tetris.model

enum class Filling {

    BOMB {
        override fun combine(filling: Filling): Filling = BOMB
        override fun fall(field: Field): Field = field
        override fun falls(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(palette: Palette, depth: Int): Color = Color.black
        override fun special(position: Position, grid: Grid): EraseResult = explode(position, grid)
        override fun toString(): String = BOMB_VALUE.toString()

        private fun explode(position: Position, grid: Grid): EraseResult = grid.erase(Grid.circle(position, 3))
    },
    SOIL {
        override fun combine(filling: Filling): Filling = SOIL
        override fun fall(field: Field): Field = field
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(palette: Palette, depth: Int): Color =
            Color.colorByDepth(palette, depth, Color.changeColorEvery)

        override fun toString(): String = SOIL_VALUE.toString()
    },
    COIN {
        override fun combine(filling: Filling): Filling = this
        override fun fall(field: Field): Field = field
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(palette: Palette, depth: Int): Color = Color.gold
        override fun score(): Int = 10
        override fun toString(): String = COIN_VALUE.toString()
    },
    FALLING {
        override fun combine(filling: Filling): Filling = FALLING
        override fun fall(field: Field): Field = field.down()
        override fun falls(): Boolean = true
        override fun isEmpty(): Boolean = false
        override fun collides(): Boolean = true
        override fun color(palette: Palette, depth: Int): Color = Color.orange
        override fun toString(): String = FILLED_VALUE.toString()
    },
    EMPTY {
        override fun combine(filling: Filling): Filling = filling
        override fun fall(field: Field): Field = field
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun score(): Int = 0
        override fun toString(): String = EMPTY_VALUE.toString()
    },
    INDENT {
        override fun combine(filling: Filling): Filling = filling
        override fun fall(field: Field): Field = field
        override fun falls(): Boolean = false
        override fun isEmpty(): Boolean = true
        override fun collides(): Boolean = false
        override fun toString(): String = INDENT_VALUE.toString()
    };

    abstract fun combine(filling: Filling): Filling
    abstract fun fall(field: Field): Field
    abstract fun falls(): Boolean
    abstract fun isEmpty(): Boolean
    abstract fun collides(): Boolean
    open fun color(palette: Palette, depth: Int) = Color.grey
    open fun score(): Int = 1
    open fun special(position: Position, grid: Grid): EraseResult = EraseResult(grid, 0)


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