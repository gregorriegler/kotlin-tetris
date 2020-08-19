import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class `A Stone` {

    @Nested
    inner class `with the size of a single field` {
        val stone = Stone(Frame(3, 3))

        @Test
        fun `starts above the center`() {
            assertPosition(
                stone,
                Field(1, -1),
                """
                ___
                ___
                ___
                """
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPosition(
                stone,
                Field(1, 0),
                """
                _#_
                ___
                ___
                """
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(3, {stone.down()})

            assertPosition(
                stone,
                Field(1, 2),
                """
                ___
                ___
                _#_
                """
            )
            assertTrue(stone.landed(Debris(Frame(3, 3))))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(4, {stone.down()})

            assertPosition(
                stone,
                Field(1, 2),
                """
                ___
                ___
                _#_
                """
            )
            assertTrue(stone.landed(Debris(Frame(3, 3))))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left()

            assertPosition(
                stone,
                Field(0, 0),
                """
                #__
                ___
                ___
                """
            )
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left()
            stone.left()

            assertPosition(
                stone,
                Field(0, 0),
                """
                #__
                ___
                ___
                """
            )
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right()

            assertPosition(
                stone,
                Field(2, 0),
                """
                __#
                ___
                ___
                """
            )
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right()
            stone.right()

            assertPosition(
                stone,
                Field(2, 0),
                """
                __#
                ___
                ___
                """
            )
        }
    }

    private fun assertPosition(stone: Stone, field: Field, state: String) {
        assertEquals(
            "\n" + state.trimIndent() + "\n",
            "\n" + Tetris.draw(stone.state()) + "\n"
        )
        assertTrue(stone.isAt(field))
    }
}