package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Filling.COIN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Rotation {

    @Test
    fun `rotates a simple position`() {
        assertThat(SimplePosition(1, 1).rotateNew()).isEqualTo(SimplePosition(1, -1))
        assertThat(SimplePosition(2, 1).rotateNew()).isEqualTo(SimplePosition(1, -2))
        assertThat(SimplePosition(2, 3).rotateNew()).isEqualTo(SimplePosition(3, -2))
    }

    @Test
    fun `rotates a Field`() {
        assertThat(Field(1, 1).rotateNew()).isEqualTo(Field(1, -1))
        assertThat(Field(2, 1).rotateNew()).isEqualTo(Field(1, -2))
        assertThat(Field(2, 3).rotateNew()).isEqualTo(Field(3, -2))
        assertThat(Field(2, 3, COIN).rotateNew()).isEqualTo(Field(3, -2, COIN))
    }
}