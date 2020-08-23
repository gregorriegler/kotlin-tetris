package com.gregorriegler.tetris.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class `A Debris` {

    @Test
    fun `can be created from multi line string`() {
        val debris = Debris("""
            ---
            -#-
            ###
        """)

        assertEquals(3, debris.width())
        assertEquals(3, debris.height())

        assertFalse(debris.collidesWith(Field.filled(0, 0)))
        assertFalse(debris.collidesWith(Field.filled(1, 0)))
        assertFalse(debris.collidesWith(Field.filled(2, 0)))
        assertFalse(debris.collidesWith(Field.filled(0, 1)))
        assertTrue(debris.collidesWith(Field.filled(1, 1)))
        assertFalse(debris.collidesWith(Field.filled(2, 1)))
        assertTrue(debris.collidesWith(Field.filled(0, 2)))
        assertTrue(debris.collidesWith(Field.filled(1, 2)))
        assertTrue(debris.collidesWith(Field.filled(2, 2)))
    }

    @Test
    fun `erases a filled line at the bottom`() {
        val debris = Debris("""
            ---
            -#-
            ###
        """)

        val howMany:Int = debris.eraseFilledRows()

        assertEquals(3, howMany)
        assertEquals(
            Debris("""
            ---
            ---
            -#-
            """),
            debris
        )
    }

    @Test
    fun `erases two filled lines at the bottom`() {
        val debris = Debris("""
            -#-
            ###
            ###
        """)

        val howMany:Int = debris.eraseFilledRows()

        assertEquals(6, howMany)
        assertEquals(
            Debris("""
            ---
            ---
            -#-
            """),
            debris
        )
    }
}