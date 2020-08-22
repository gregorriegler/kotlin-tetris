class Stone(
    structure: Structure,
    private val frame: Frame,
) {
    constructor(frame: Frame) : this(Structure("#"), frame)
    constructor(structure: String, frame: Frame) : this(Structure(structure), frame)

    var area: Area = structure.aboveCentered(Area(frame))
        private set

    fun down() {
        area = frame.down(area)
    }

    fun left(debris: Debris) {
        area = frame.left(area, debris)
    }

    fun right(debris: Debris) {
        area = frame.right(area, debris)
    }

    fun rotate(debris: Debris) {
        var rotate = area.rotate()

        if(rotate.bottomOfFilled() > frame.height - 1 || debris.collidesWith(rotate)){
            return
        }

        val howMuchOutsideRight = rotate.rightSideOfFilled() - (frame.width - 1)
        if(howMuchOutsideRight > 0) { // we're outside right
            if(rotate.filledWidth() <= frame.width) { // there is enough room to the left
                rotate = rotate.left(howMuchOutsideRight)
            }
        }

        val howMuchOutsideLeft = rotate.leftSideOfFilled() - 0
        if(howMuchOutsideLeft < 0) { // we're outside left
            if(rotate.filledWidth() <= frame.width) { // there is enough room to the right
                rotate = rotate.right(howMuchOutsideLeft * -1)
            }
        }

        area = rotate
    }

    fun state(): List<List<Filling>> =
        frame.rows().map { y ->
            frame.columns().map { x ->
                area.fillingOf(x, y)
            }
        }.toList()

    fun landed(debris: Debris) = atBottom() || collidesWith(debris)
    private fun atBottom(): Boolean = frame.isAtBottom(area)
    private fun collidesWith(debris: Debris) = debris.collidesWith(area.down())
}