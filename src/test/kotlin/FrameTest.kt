import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Frame` {

    @Test
    fun `returns the field left of a field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(0, 1), frame.leftOf(Field(1,1)))
    }

    @Test
    fun `respects the left wall`() {
        val frame = Frame(3, 3)
        assertEquals(Field(0, 1), frame.leftOf(Field(0,1)))
    }

    @Test
    fun `returns the field right of a field`() {
        val frame = Frame(3, 3)
        assertEquals(Field(2, 1), frame.rightOf(Field(1,1)))
    }

    @Test
    fun `respects the right wall`() {
        val frame = Frame(3, 3)
        assertEquals(Field(2, 1), frame.rightOf(Field(2,1)))
    }
}