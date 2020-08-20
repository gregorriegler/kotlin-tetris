import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Field` {

    @Test
    fun `returns the field below`() {
        assertEquals(Field(1, 2), Field(1,1).below())
    }

    @Test
    fun `returns the field to the left`() {
        assertEquals(Field(0, 1), Field(1,1).toTheLeft())
    }

    @Test
    fun `returns the field to the right`() {
        assertEquals(Field(2, 1), Field(1,1).toTheRight())
    }

    @Test
    fun `adds fields`() {
        assertEquals(Field(2, 2), Field(1,1).plus(Field(1,1)))
    }

    @Test
    fun `rotates a field`() {
        //top row
        assertEquals(Field(2, 0), Field(0,0).rotate(3, 3))
        assertEquals(Field(2, 1), Field(1,0).rotate(3, 3))
        assertEquals(Field(2, 2), Field(2,0).rotate(3, 3))

        //center row
        assertEquals(Field(1, 0), Field(0,1).rotate(3, 3))
        assertEquals(Field(1, 1), Field(1,1).rotate(3, 3))
        assertEquals(Field(1, 2), Field(2,1).rotate(3, 3))

        //bottom row
        assertEquals(Field(0, 0), Field(0,2).rotate(3, 3))
        assertEquals(Field(0, 1), Field(1,2).rotate(3, 3))
        assertEquals(Field(0, 2), Field(2,2).rotate(3, 3))
    }
}