package com.gregorriegler.tetris.model

class Structure(string: String) : Area(string) {
    companion object {
        fun create1by1(): Structure {
            return Structure("#")
        }

        fun create1by5(): Structure {
            return Structure("""
                -#--
                -#--
                -#--
                -#--
            """.trimIndent())
        }

        fun create2by2(): Structure {
            return Structure("""
                ##
                ##
            """)
        }

        fun create3and1(): Structure {
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

        fun createMirrorL(): Structure {
            return Structure("""
                ##-
                #--
                #--
            """.trimIndent())
        }
    }
}
