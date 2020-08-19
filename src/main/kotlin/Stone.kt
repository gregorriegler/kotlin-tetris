class Stone(
    private val frame: Frame
) {
    private var field:Field = frame.startingField()

    fun down() {
        field = field.below()
    }

    fun left() {
        field = frame.leftOf(field)
    }

    fun right() {
        field = frame.rightOf(field)
    }

    fun landed(debris: Debris) = atBottom() || collisionWith(debris)

    fun isAt(x: Int, y: Int): Boolean {
        return Field(x, y) == field
    }

    private fun atBottom(): Boolean = frame.isAtBottom(field)
    private fun collisionWith(debris: Debris) = debris.hasDebris(field.below())
}