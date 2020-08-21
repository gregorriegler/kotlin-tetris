import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class `An Area` {

    @Test
    fun `can be created from a string`() {
        val area = Area("""
            ##
            ##
        """
        )

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.covers(Field(0, 0)))
        assertTrue(area.covers(Field(1, 0)))
        assertTrue(area.covers(Field(0, 1)))
        assertTrue(area.covers(Field(1, 1)))
        assertFalse(area.covers(Field(2, 1)))
    }

    @Test
    fun `can be created from a string #2`() {
        val area = Area("""
            #_
            ##
        """
        )

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.covers(Field(0, 0)))
        assertFalse(area.covers(Field(1, 0)))
        assertTrue(area.covers(Field(0, 1)))
        assertTrue(area.covers(Field(1, 1)))
        assertFalse(area.covers(Field(2, 1)))
    }

    @Test
    fun `can be created from a string #3`() {
        val area = Area("""
            ___
            _#_
            ___
        """
        )

        assertEquals(1, area.width())
        assertEquals(1, area.height())
        assertFalse(area.covers(Field(0, 0)))
        assertFalse(area.covers(Field(1, 0)))
        assertFalse(area.covers(Field(2, 0)))
        assertFalse(area.covers(Field(0, 1)))
        assertTrue(area.covers(Field(1, 1)))
        assertFalse(area.covers(Field(2, 1)))
        assertFalse(area.covers(Field(0, 2)))
        assertFalse(area.covers(Field(1, 2)))
        assertFalse(area.covers(Field(2, 2)))
    }

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
            Area("""
                ___
                _##
            """),
            Area("""
                _##
            """).down()
        )
    }

    @Test
    fun `moves left`() {
        assertEquals(
            Area("##_"), Area("_##").left()
        )
    }

    @Test
    fun `moves right`() {
        assertEquals(
            Area("_##"), Area("##_").right()
        )
    }

    @Test
    fun rotates() {
        assertThat(Area("""
                ___
                ###
                ___
                """).rotate())
            .isEqualTo(Area("""
                _#_
                _#_
                _#_
                """))

        assertThat(
            Area("""
                ___
                ###
                _#_
                """).rotate())
            .isEqualTo(
                Area("""
                _#_
                ##_
                _#_
                """))

        assertThat(
            Area("""
                ##_
                #__
                #__
                """).rotate())
            .isEqualTo(
                Area("""
                ###
                __#
                ___
                """))
    }
}

