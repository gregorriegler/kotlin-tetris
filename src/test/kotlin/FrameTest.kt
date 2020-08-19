import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
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
        assertEquals(Area(Field(1, -1),Field(2, -1)), Frame(4, 4).startingArea(Structure(Field(0, 0), Field(1, 0))))
        assertEquals(Area(Field(1, -1),Field(2, -1)), Frame(4, 4).startingArea(Structure(Field(0, 0), Field(1, 0))))
        assertEquals(Area(Field(0, -1), Field(1, -1), Field(2, -1)), Frame(3, 3).startingArea(Structure(Field(0, 0), Field(1, 0), Field(2, 0))))
    }

    @Test
    fun `returns the top center field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(1, 0), frame.topCenter())
    }

    @Test
    fun `returns the field left of a field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(0, 1), frame.leftOf(Field(1, 1)))
    }

    @Test
    fun `respects the left wall`() {
        val frame = Frame(3, 3)
        assertEquals(Field(0, 1), frame.leftOf(Field(0, 1)))
    }

    @Test
    fun `returns the field right of a field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(2, 1), frame.rightOf(Field(1, 1)))
    }

    @Test
    fun `respects the right wall`() {
        val frame = Frame(3, 3)
        assertEquals(Field(2, 1), frame.rightOf(Field(2, 1)))
    }

    @Test
    fun `knows a field is not at the bottom`() {
        assertEquals(false, Frame(3, 3).isAtBottom(Field(1, 1)))
    }

    @Test
    fun `knows a field is at the bottom`() {
        assertEquals(true, Frame(3, 3).isAtBottom(Field(1, 2)))
    }
}