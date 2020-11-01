package com.gregorriegler.tetris.model

class Structure(string: String) : Grid(string) {
    companion object {
        fun create1by1(): Structure {
            return Structure("#")
        }

        fun createI(): Structure {
            return Structure(
                """
                -#--
                -#--
                -#--
                -#--
                """
            )
        }

        fun createDot(): Structure {
            return Structure(
                """
                ##
                ##
                """
            )
        }

        fun createT(): Structure {
            return Structure(
                """
                -#-
                -##
                -#-
                """
            )
        }

        fun createL(): Structure {
            return Structure(
                """
                #--
                #--
                ##-
                """
            )
        }

        fun createJ(): Structure {
            return Structure(
                """
                ##-
                #--
                #--
                """
            )
        }

        fun createBomb(): Structure {
            return Structure(
                """
                ---
                -X-
                ---
                """
            )
        }
    }

    fun startingPosition(grid: Grid): Grid = move(Position.of((grid.width - width) / 2, grid.y - height))
}
