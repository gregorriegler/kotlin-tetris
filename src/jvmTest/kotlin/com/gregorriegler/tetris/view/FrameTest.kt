package com.gregorriegler.tetris.view

import com.gregorriegler.tetris.model.SimpleFrame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FrameTest {
    @Test
    fun `creates max that fits within height`() {
        val max = SimpleFrame.max(SimpleFrame(20, 100), SimpleFrame(200, 200))
        assertThat(max).isEqualTo(SimpleFrame(40, 200))
    }
}