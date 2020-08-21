enum class Filling {

    FILLED {
        override fun toString(): String {
            return FILLED_VALUE
        }
    },
    EMPTY {
        override fun toString(): String {
            return EMPTY_VALUE
        }
    },
    INDENT {
        override fun toString(): String {
            return INDENT_VALUE
        }
    };

    companion object {
        const val FILLED_VALUE: String = "#"
        const val EMPTY_VALUE: String = "_"
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