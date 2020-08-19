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

    @Test
    fun `has a bottom`() {
        assertEquals(0, Area(Field(0, 0)).bottom())
        assertEquals(1, Area(Field(0, 0), Field(0, 1)).bottom())
    }

    @Test
    fun `has a left side`() {
        assertEquals(0, Area(Field(0, 0)).leftSide())
        assertEquals(-1, Area(Field(-1, 0), Field(0, 0)).leftSide())
    }

    @Test
    fun `has a right side`() {
        assertEquals(0, Area(Field(0, 0)).rightSide())
        assertEquals(1, Area(Field(0, 0), Field(1, 0)).rightSide())
    }

    @Test
    fun `covers a field`() {
        assertTrue(Area(Field(0, 0)).covers(Field(0, 0)))
        assertFalse(Area(Field(0, 0)).covers(Field(1, 1)))
    }

    @Test
    fun `moves down`() {
        assertEquals(
            Area(
                Field(1, 1),
                Field(2, 1),
                Field(1, 2),
                Field(2, 2),
            ),
            Area(
                Field(1, 0),
                Field(2, 0),
                Field(1, 1),
                Field(2, 1),
            ).down()
        )
    }

    @Test
    fun `moves left`() {
        assertEquals(
            Area(
                Field(0, 1),
                Field(1, 1),
                Field(0, 2),
                Field(1, 2),
            ),
            Area(
                Field(1, 1),
                Field(2, 1),
                Field(1, 2),
                Field(2, 2),
            ).left()
        )
    }

    @Test
    fun `moves right`() {
        assertEquals(
            Area(
                Field(2, 1),
                Field(3, 1),
                Field(2, 2),
                Field(3, 2),
            ),
            Area(
                Field(1, 1),
                Field(2, 1),
                Field(1, 2),
                Field(2, 2),
            ).right()
        )
    }
}

