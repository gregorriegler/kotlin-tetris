class Structure(vararg field: Field) : Area(*field) {
    companion object {
        fun create2by2(): Structure {
            return Structure(
                Field(0, 0),
                Field(1, 0),
                Field(0, 1),
                Field(1, 1)
            )
        }
    }
}
