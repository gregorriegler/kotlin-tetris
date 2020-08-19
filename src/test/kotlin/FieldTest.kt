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
}