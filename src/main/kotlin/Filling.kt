enum class Filling {
    FILLED {
        override fun toString(): String {
            return "#"
        }
    },
    EMPTY {
        override fun toString(): String {
            return "_"
        }
    },
    INDENT {
        override fun toString(): String {
            return ">"
        }
    }
}