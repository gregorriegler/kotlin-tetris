package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
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

        assertFalse(Field.filled(0, 0).collidesWith(debris.area))
        assertFalse(Field.filled(1, 0).collidesWith(debris.area))
        assertFalse(Field.filled(2, 0).collidesWith(debris.area))
        assertFalse(Field.filled(0, 1).collidesWith(debris.area))
        assertTrue(Field.filled(1, 1).collidesWith(debris.area))
        assertFalse(Field.filled(2, 1).collidesWith(debris.area))
        assertTrue(Field.filled(0, 2).collidesWith(debris.area))
        assertTrue(Field.filled(1, 2).collidesWith(debris.area))
        assertTrue(Field.filled(2, 2).collidesWith(debris.area))
    }
}