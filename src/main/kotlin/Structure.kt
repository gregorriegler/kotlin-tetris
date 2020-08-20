class Structure(string: String) : Area(string) {
    companion object {
        fun create1by4(): Structure {
            return Structure("""
                #
                #
                #
                #
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
                #_
                ##
                #_
            """)
        }
    }
}
