import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Structure` {

    @Test
    fun `has a width`() {
        assertEquals(1, Structure(Field(0, 0)).width())
        assertEquals(2, Structure(Field(0, 0), Field(1,0)).width())
    }

    @Test
    fun `has a height`() {
        assertEquals(1, Structure(Field(0, 0)).height())
        assertEquals(2, Structure(Field(0, 0), Field(0, 1)).height())
    }
}

class Structure(private vararg val field: Field) {
    fun width(): Int {
        val left = field.map { it.x }.minOrNull()!!
        val right = field.map { it.x }.maxOrNull()!!
        return (right - left) + 1
    }

    fun height(): Int {
        val top = field.map { it.y }.minOrNull()!!
        val bottom = field.map { it.y }.maxOrNull()!!
        return (bottom - top) + 1
    }

}
