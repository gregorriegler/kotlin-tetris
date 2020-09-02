package com.gregorriegler.tetris.view

import com.gregorriegler.tetris.model.SimpleFrame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ViewFrameTest {
    @Test
    fun `creates max that fits within height`() {
        val max = PositionedFrame.max(SimpleFrame(20, 100), SimpleFrame(200, 200))
        assertThat(max).isEqualTo(PositionedFrame(80,0,40, 200))
    }
}