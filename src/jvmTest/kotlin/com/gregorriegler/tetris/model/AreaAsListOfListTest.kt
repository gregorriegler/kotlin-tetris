package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AreaAsListOfListTest {

    @Test
    fun `can be created from a string`() {
        val area = AreaAsListOfList("""
            ##
            --
        """)
        assertThat(area.get(0,0)).isEqualTo(Field.filled(0, 0))
        assertThat(area.get(1,0)).isEqualTo(Field.filled(1, 0))
        assertThat(area.get(0,1)).isEqualTo(Field.empty(0, 1))
        assertThat(area.get(1,1)).isEqualTo(Field.empty(1, 1))
    }

}

