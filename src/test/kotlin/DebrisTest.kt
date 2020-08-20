import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Debris` {

    @Test
    fun `can be created from multi line string`() {
        val debris = Debris("""
            ___
            _#_
            ###
        """)

        assertEquals(3, debris.width())
        assertEquals(3, debris.height())

        assertFalse(debris.isAt(Field(0, 0)))
        assertFalse(debris.isAt(Field(1, 0)))
        assertFalse(debris.isAt(Field(2, 0)))
        assertFalse(debris.isAt(Field(0, 1)))
        assertTrue(debris.isAt(Field(1, 1)))
        assertFalse(debris.isAt(Field(2, 1)))
        assertTrue(debris.isAt(Field(0, 2)))
        assertTrue(debris.isAt(Field(1, 2)))
        assertTrue(debris.isAt(Field(2, 2)))
    }

    @Test
    fun `dissolves a filled line at the bottom`() {
        val debris = Debris("""
            ___
            _#_
            ###
        """)

        val howMany:Int = debris.dissolveFilledRows()

        assertEquals(1, howMany)
        assertEquals(
            Debris("""
            ___
            ___
            _#_
            """),
            debris
        )
    }

    @Test
    fun `dissolves two filled lines at the bottom`() {
        val debris = Debris("""
            _#_
            ###
            ###
        """)

        val howMany:Int = debris.dissolveFilledRows()

        assertEquals(2, howMany)
        assertEquals(
            Debris("""
            ___
            ___
            _#_
            """),
            debris
        )
    }
}