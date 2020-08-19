class Debris(
    private val frame: Frame,
) {
    private val debris: List<List<String>> = frame.empty()

    fun hasDebris(field: Field): Boolean {
        return debris[field.y][field.x] != Field.EMPTY
    }
}