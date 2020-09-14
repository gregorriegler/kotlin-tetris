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

        assertFalse(Field.filled(Position.of(0, 0)).collidesWith(debris.area))
        assertFalse(Field.filled(Position.of(1, 0)).collidesWith(debris.area))
        assertFalse(Field.filled(Position.of(2, 0)).collidesWith(debris.area))
        assertFalse(Field.filled(Position.of(0, 1)).collidesWith(debris.area))
        assertTrue(Field.filled(Position.of(1, 1)).collidesWith(debris.area))
        assertFalse(Field.filled(Position.of(2, 1)).collidesWith(debris.area))
        assertTrue(Field.filled(Position.of(0, 2)).collidesWith(debris.area))
        assertTrue(Field.filled(Position.of(1, 2)).collidesWith(debris.area))
        assertTrue(Field.filled(Position.of(2, 2)).collidesWith(debris.area))
    }
}