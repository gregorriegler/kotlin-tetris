package com.gregorriegler.tetris.model

import com.gregorriegler.tetris.model.Timer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class `A GameClock` {

    var called = 0
    val consumerSpy = {
        called += 1
    }
    val gameClock = Timer(consumerSpy)

    @Test
    fun `does not tick too quickly`() {
        gameClock.time(50)

        assertEquals(0, called)
    }

    @Test
    fun `ticks on time`() {
        gameClock.time(250)

        assertEquals(1, called)
    }

    @Test
    fun `ticks after time`() {
        gameClock.time(255)

        assertEquals(1, called)
    }

    @Test
    fun `does not tick a second time before 500`() {
        gameClock.time(250)
        gameClock.time(300)

        assertEquals(1, called)
    }

    @Test
    fun `ticks on every 250`() {
        gameClock.time(250)
        gameClock.time(500)

        assertEquals(2, called)
    }

    @Test
    fun `speeds up`() {
        gameClock.speed()
        gameClock.time(50)
        gameClock.time(100)

        assertEquals(2, called)
    }

    @Test
    fun `back to normal`() {
        gameClock.speed()
        gameClock.time(50)
        gameClock.time(100)
        gameClock.normal()
        gameClock.time(200)
        gameClock.time(350)

        assertEquals(3, called)
    }
}

