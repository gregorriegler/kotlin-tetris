import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class `A Field` {

    @Test
    fun `returns the field below`() {
        assertThat(Field(1,1).below()).isEqualTo(Field(1, 2))
    }

    @Test
    fun `returns the field to the left`() {
        assertThat(Field(1,1).toTheLeft()).isEqualTo(Field(0, 1))
    }

    @Test
    fun `returns the field to the right`() {
        assertThat(Field(1,1).toTheRight()).isEqualTo(Field(2, 1))
    }

    @Test
    fun `adds a field`() {
        assertThat(Field(1,1).plus(Field(1,1))).isEqualTo(Field(2, 2))
    }

    @Test
    fun `minus a field`() {
        assertThat(Field(1,1).minus(Field(1,1))).isEqualTo(Field(0, 0))
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