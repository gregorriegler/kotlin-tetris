import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
        assertTrue(area.has(Field.filled(0, 0)))
        assertTrue(area.has(Field.filled(1, 0)))
        assertTrue(area.has(Field.filled(0, 1)))
        assertTrue(area.has(Field.filled(1, 1)))
        assertFalse(area.has(Field.filled(2, 1)))
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
        assertTrue(area.has(Field.filled(0, 0)))
        assertFalse(area.has(Field.filled(1, 0)))
        assertTrue(area.has(Field.filled(0, 1)))
        assertTrue(area.has(Field.filled(1, 1)))
        assertFalse(area.has(Field.filled(2, 1)))
    }

    @Test
    fun `can be created from a string #3`() {
        val area = Area("""
            ___
            _#_
            ___
        """
        )

        assertEquals(3, area.width())
        assertEquals(3, area.height())
        assertFalse(area.has(Field.filled(0, 0)))
        assertFalse(area.has(Field.filled(1, 0)))
        assertFalse(area.has(Field.filled(2, 0)))
        assertFalse(area.has(Field.filled(0, 1)))
        assertTrue(area.has(Field.filled(1, 1)))
        assertFalse(area.has(Field.filled(2, 1)))
        assertFalse(area.has(Field.filled(0, 2)))
        assertFalse(area.has(Field.filled(1, 2)))
        assertFalse(area.has(Field.filled(2, 2)))
    }

    @Test
    fun `can be indented on the x axis`() {
        val area = Area("""
            >##
            >##
        """)

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.has(Field.filled(1, 0)))
        assertTrue(area.has(Field.filled(2, 0)))
        assertTrue(area.has(Field.filled(1, 1)))
        assertTrue(area.has(Field.filled(2, 1)))
        assertFalse(area.has(Field.filled(0, 1)))
    }

    @Test
    fun `can be indented on the y axis`() {
        val area = Area("""
            >>
            ##
            ##
        """)

        assertEquals(2, area.width())
        assertEquals(2, area.height())
        assertTrue(area.has(Field.filled(0, 1)))
        assertTrue(area.has(Field.filled(1, 1)))
        assertTrue(area.has(Field.filled(0, 2)))
        assertTrue(area.has(Field.filled(1, 2)))
        assertFalse(area.has(Field.filled(0, 0)))
    }

    @Test
    fun `has a width`() {
        assertEquals(1, Area("#").width())
        assertEquals(2, Area("##").width())
    }

    @Test
    fun `has a height`() {
        assertEquals(1, Area("#").height())
        assertEquals(2, Area("#\n#").height())
    }

    @Test
    fun `has a bottom`() {
        assertEquals(0, Area("#").bottomOfFilled())
        assertEquals(1, Area("#\n#").bottomOfFilled())
    }

    @Test
    fun `has a left side`() {
        assertEquals(0, Area("#").leftSideOfFilled())
        assertEquals(1, Area("_##").leftSideOfFilled())
    }

    @Test
    fun `has a right side`() {
        assertEquals(0, Area("#").rightSideOfFilled())
        assertEquals(1, Area("##").rightSideOfFilled())
    }

    @Test
    fun `covers a field`() {
        assertTrue(Area("#").has(Field.filled(0, 0)))
        assertFalse(Area("#").has(Field.filled(1, 1)))
    }

    @Test
    fun `moves down`() {
        assertThat(
            Area("""
                _##
            """).down()
        ).isEqualTo(
            Area("""
                >>>
                _##
            """)
        )
    }

    @Test
    fun `moves left`() {
        assertThat(Area("_##").left()).isEqualTo(Area(
            Field.empty(-1, 0), Field.filled(0, 0), Field.filled(1, 0)
        ))
    }

    @Test
    fun `moves right`() {
        assertThat(Area("##_").right()).isEqualTo(Area(">##_"))
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

        assertThat(Area("""
                ___
                ###
                _#_
                """).rotate())
            .isEqualTo(Area("""
                _#_
                ##_
                _#_
                """))

        assertThat(Area("""
                >>>
                >>>
                ##_
                #__
                #__
                """).rotate())
            .isEqualTo(Area("""
                >>>
                >>>
                ###
                __#
                ___
                """))
    }
}

