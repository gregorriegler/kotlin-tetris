import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Frame` {

    @Test
    fun `returns the starting area for a structure`() {
        assertThat(
            Frame(3, 3).startingArea(Structure("#"))
        ).isEqualTo(Area(filledField(1, -1)))
        assertEquals(Area(filledField(1, -1)), Frame(4, 4).startingArea(Structure("#")))
        assertEquals(Area(filledField(1, -2), filledField(1, -1)), Frame(3, 3).startingArea(Structure("#\n#")))
        assertEquals(Area(filledField(1, -1), filledField(2, -1)), Frame(4, 4).startingArea(Structure("##")))
        assertEquals(Area(filledField(1, -1), filledField(2, -1)), Frame(4, 4).startingArea(Structure("##")))
        assertEquals(Area(filledField(0, -1), filledField(1, -1), filledField(2, -1)),
            Frame(3, 3).startingArea(Structure("###")))
    }

    private fun filledField(x: Int, y: Int) = Field.filled(x, y)

    @Test
    fun `returns the top center field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(1, 0), frame.topCenter())
    }

    @Test
    fun `moves an area to the left`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area(Field.empty(-1, 0), Field.filled(0, 0), Field.filled(1, 0)),
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

        val x = -1
        val y = 0
        val expected = Area(
            Field.empty(x, y),
            Field.empty(0, 0),
            Field.empty(1, 0),
            Field.filled(2, 0),
            Field.empty(-1, 1),
            Field.empty(0, 1),
            Field.filled(1, 1),
            Field.filled(2, 1),
            Field.empty(-1, 2),
            Field.empty(0, 2),
            Field.empty(1, 2),
            Field.filled(2, 2),
        )
        val actual = frame.left(area, debris)
        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun `moves an area to the right`() {
        val frame = Frame(3, 3)
        assertThat(
            frame.right(Area("##_"), Debris(frame))
        ).isEqualTo(Area(">##_"))
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
                >#___
                >##__
                >#___
                """),
            frame.right(area, debris)
        )
    }

    @Test
    fun `moves an area down`() {
        val frame = Frame(3, 3)
        assertEquals(
            Area("""
                >>
                ##
                __
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
        assertFalse(Frame(3, 3).isAtBottom(Area(Field.filled(1, 1))))
    }

    @Test
    fun `knows an area is at the bottom`() {
        assertTrue(Frame(2, 2).isAtBottom(Area(Field.filled(1, 1))))
    }
}