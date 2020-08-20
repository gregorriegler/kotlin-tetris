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
        assertEquals(Area(Field(1, -1)), Frame(3, 3).startingArea(Structure("#")))
        assertEquals(Area(Field(1, -1)), Frame(4, 4).startingArea(Structure("#")))
        assertEquals(Area(Field(1, -2), Field(1, -1)), Frame(3, 3).startingArea(Structure("#\n#")))
        assertEquals(Area(Field(1, -1), Field(2, -1)), Frame(4, 4).startingArea(Structure("##")))
        assertEquals(Area(Field(1, -1), Field(2, -1)), Frame(4, 4).startingArea(Structure("##")))
        assertEquals(Area(Field(0, -1), Field(1, -1), Field(2, -1)),
            Frame(3, 3).startingArea(Structure("###")))
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
            Area("""
                ##_
                """),
            frame.left(
                Area("""
                _##
                """),
                Debris(Frame(3, 3))
            )
        )
    }

    @Test
    fun `respects the left wall when moving left`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area("""
                ##_
                """
            ),
            frame.left(
                Area("""
                ##_
                """
                ),
                Debris(Frame(3, 3))
            )
        )
    }

    @Test
    fun `respects debris when moving left`() {
        val frame = Frame(3, 3)
        val area = Area("""
                _##
                """)
        val debris = Debris("""
                #__
                """)
        assertEquals(area, frame.left(area, debris))
    }

    @Test
    fun `moves inside debris when moving left`() {
        val frame = Frame(4, 3)
        val area = Area("""
                ___#
                __##
                ___#
                """)
        val debris = Debris("""
                ##__
                #___
                ##__
                """)

        assertEquals(
            Area("""
                __#_
                _##_
                __#_
                """),
            frame.left(area, debris)
        )
    }

    @Test
    fun `moves an area to the right`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area("""
                _##
                """),
            frame.right(
                Area("""
                ##_
                """),
                Debris(frame)
            )
        )
    }

    @Test
    fun `respects the right wall when moving right`() {
        val frame = Frame(2, 2)
        assertEquals(
            Area("""
                _#
                _#
                """
            ),
            frame.right(
                Area("""
                _#
                _#
                """
                ),
                Debris(frame)
            )
        )
    }

    @Test
    fun `respects debris when moving right`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area("""
                ___
                ##_
                """
            ),
            frame.right(
                Area("""
                ___
                ##_
                """
                ),
                Debris("""
                __#
                __#
                __#
                """)
            ))
    }

    @Test
    fun `moves inside debris when moving right`() {
        val frame = Frame(4, 3)
        val area = Area("""
                #___
                ##__
                #___
                """)
        val debris = Debris("""
                __##
                ___#
                __##
                """)

        assertEquals(
            Area("""
                _#__
                _##_
                _#__
                """),
            frame.right(area, debris)
        )
    }

    @Test
    fun `moves an area down`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area("""
                __
                ##
                """
            ),
            frame.down(
                Area("""
                    ##
                    __
                    """
                )
            )
        )
    }

    @Test
    fun `does not move an area below the bottom`() {
        val frame = Frame(2, 2)
        assertEquals(
            Area("""
                __
                ##
                """
            ),
            frame.down(
                Area("""
                __
                ##
                """
                )
            )
        )
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