import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Frame` {

    @Test
    fun `returns the starting field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(1, -1), frame.startingField())
    }

    @Test
    fun `returns the starting area for a structure`() {
        assertEquals(Area(Field(1, -1)), Frame(3, 3).startingArea(Structure(Field(0, 0))))
        assertEquals(Area(Field(1, -1)), Frame(4, 4).startingArea(Structure(Field(0, 0))))
        assertEquals(Area(Field(1, -2), Field(1, -1)), Frame(3, 3).startingArea(Structure(Field(0, 0), Field(0, 1))))
        assertEquals(Area(Field(1, -1), Field(2, -1)), Frame(4, 4).startingArea(Structure(Field(0, 0), Field(1, 0))))
        assertEquals(Area(Field(1, -1), Field(2, -1)), Frame(4, 4).startingArea(Structure(Field(0, 0), Field(1, 0))))
        assertEquals(Area(Field(0, -1), Field(1, -1), Field(2, -1)),
            Frame(3, 3).startingArea(Structure(Field(0, 0), Field(1, 0), Field(2, 0))))
    }

    @Test
    fun `returns the top center field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(1, 0), frame.topCenter())
    }

    @Test
    fun `moves an area to the left`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(0, 1),
                Field(1, 1),
                Field(0, 2),
                Field(1, 2),
            ),
            frame.left(
                Area(
                    Field(1, 1),
                    Field(2, 1),
                    Field(1, 2),
                    Field(2, 2),
                ),
                Debris(Frame(3, 3))
            )
        )
    }

    @Test
    fun `respects the left wall when moving left`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(0, 1),
                Field(1, 1),
                Field(0, 2),
                Field(1, 2),
            ),
            frame.left(
                Area(
                    Field(0, 1),
                    Field(1, 1),
                    Field(0, 2),
                    Field(1, 2),
                ),
                Debris(Frame(3, 3))
            )
        )
    }

    @Test
    fun `respects debris when moving left`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(1, 1),
                Field(2, 1),
                Field(1, 2),
                Field(2, 2),
            ),
            frame.left(
                Area(
                    Field(1, 1),
                    Field(2, 1),
                    Field(1, 2),
                    Field(2, 2)
                ),
                Debris("""
                    #__
                    #__
                    #__
                """.trimIndent())
            ))
    }

    @Test
    fun `moves an area to the right`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(1, 1),
                Field(2, 1)
            ),
            frame.right(
                Area(
                    Field(0, 1),
                    Field(1, 1)
                ),
                Debris(frame)
            )
        )
    }

    @Test
    fun `respects the right wall when moving right`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(1, 1),
                Field(2, 1)
            ),
            frame.right(
                Area(
                    Field(1, 1),
                    Field(2, 1)
                ),
                Debris(frame)
            )
        )
    }

    @Test
    fun `respects debris when moving right`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(
                Field(0, 1),
                Field(1, 1),
                Field(0, 2),
                Field(1, 2),
            ),
            frame.right(
                Area(
                    Field(0, 1),
                    Field(1, 1),
                    Field(0, 2),
                    Field(1, 2)
                ),
                Debris("""
                    __#
                    __#
                    __#
                """.trimIndent())
            ))
    }

    @Test
    fun `moves an area down`() {
        val frame = Frame(3, 3)
        assertEquals(Area(
            Field(1, 1),
            Field(2, 1),
            Field(1, 2),
            Field(2, 2),
        ), frame.down(Area(
            Field(1, 0),
            Field(2, 0),
            Field(1, 1),
            Field(2, 1),
        )))
    }

    @Test
    fun `does not move an area below the bottom`() {
        val frame = Frame(3, 3)
        assertEquals(Area(
            Field(1, 1),
            Field(2, 1),
            Field(1, 2),
            Field(2, 2),
        ), frame.down(Area(
            Field(1, 1),
            Field(2, 1),
            Field(1, 2),
            Field(2, 2),
        )))
    }

    @Test
    fun `knows an area is not at the bottom`() {
        assertEquals(false, Frame(3, 3).isAtBottom(Area(Field(1, 1))))
    }

    @Test
    fun `knows an area is at the bottom`() {
        assertEquals(false, Frame(3, 3).isAtBottom(Area(Field(1, 1))))
    }
}