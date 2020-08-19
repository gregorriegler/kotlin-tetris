import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `An Area` {

    @Test
    fun `has a width`() {
        assertEquals(1, Area(Field(0, 0)).width())
        assertEquals(2, Area(Field(0, 0), Field(1, 0)).width())
    }

    @Test
    fun `has a height`() {
        assertEquals(1, Area(Field(0, 0)).height())
        assertEquals(2, Area(Field(0, 0), Field(0, 1)).height())
    }
}

