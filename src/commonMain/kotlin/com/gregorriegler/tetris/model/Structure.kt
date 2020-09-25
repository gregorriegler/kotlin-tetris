package com.gregorriegler.tetris.model

class Structure(string: String) : Area(string) {
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
            return Structure("""
                ---
                -X-
                ---
                """)
        }
    }

    fun startingPosition(area: Area): Area = move(Position.of((area.width - width) / 2, area.y - height))
}
