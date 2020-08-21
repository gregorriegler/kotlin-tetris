import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

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
    fun `adds a field`() {
        assertEquals(Field(2, 2), Field(1,1).plus(Field(1,1)))
    }

    @Test
    fun `minus a field`() {
        assertEquals(Field(0, 0), Field(1,1).minus(Field(1,1)))
    }

    @Test
    fun `rotates a field in a 3x3 block`() {
        //top row
        assertThat(Field(0,0).rotate(3)).isEqualTo(Field(2, 0))
        assertThat(Field(1,0).rotate(3)).isEqualTo(Field(2, 1))
        assertThat(Field(2,0).rotate(3)).isEqualTo(Field(2, 2))
        //center row
        assertThat(Field(0,1).rotate(3)).isEqualTo(Field(1, 0))
        assertThat(Field(1,1).rotate(3)).isEqualTo(Field(1, 1))
        assertThat(Field(2,1).rotate(3)).isEqualTo(Field(1, 2))
        //bottom row
        assertThat(Field(0,2).rotate(3)).isEqualTo(Field(0, 0))
        assertThat(Field(1,2).rotate(3)).isEqualTo(Field(0, 1))
        assertThat(Field(2,2).rotate(3)).isEqualTo(Field(0, 2))
    }
}