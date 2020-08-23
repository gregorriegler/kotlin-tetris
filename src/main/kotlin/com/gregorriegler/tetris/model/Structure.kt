package com.gregorriegler.tetris.model

class Structure(string: String) : Area(string) {
    companion object {
        fun create1by1(): Structure {
            return Structure("#")
        }

        fun createI(): Structure {
            return Structure("""
                -#--
                -#--
                -#--
                -#--
            """.trimIndent())
        }

        fun createDot(): Structure {
            return Structure("""
                ##
                ##
            """)
        }

        fun createT(): Structure {
            return Structure("""
                -#-
                -##
                -#-
            """)
        }

        fun createL(): Structure {
            return Structure("""
                #--
                #--
                ##-
            """.trimIndent())
        }

        fun createJ(): Structure {
            return Structure("""
                ##-
                #--
                #--
            """.trimIndent())
        }
    }
}
