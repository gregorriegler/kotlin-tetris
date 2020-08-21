import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class `A Stone` {

    val frame = Frame(3, 3)
    val debris3x3 = Debris(frame)
    val debris4x4 = Debris(Frame(4, 4))

    @Nested
    inner class `with the size of a single field` {
        val stone = Stone(frame)

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
            repeat(3, { stone.down() })

            assertPosition(
                stone,
                Field(1, 2),
                """
                ___
                ___
                _#_
                """
            )
            assertTrue(stone.landed(debris3x3))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(4, { stone.down() })

            assertPosition(
                stone,
                Field(1, 2),
                """
                ___
                ___
                _#_
                """
            )
            assertTrue(stone.landed(Debris(frame)))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris3x3)

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
            stone.left(debris3x3)
            stone.left(debris3x3)

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
            stone.right(debris3x3)

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
            stone.right(debris3x3)
            stone.right(debris3x3)

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

    @Nested
    inner class `with a size 2x2` {
        val stone = Stone(Structure.create2by2(), Frame(4, 4))

        @Test
        fun `starts above the center`() {
            assertPositionArea(
                stone,
                Area(
                    Field(1, -2),
                    Field(2, -2),
                    Field(1, -1),
                    Field(2, -1)
                ),
                """
                ____
                ____
                ____
                ____
                """
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPositionArea(
                stone,
                Area(
                    Field(1, -1),
                    Field(2, -1),
                    Field(1, -0),
                    Field(2, -0)
                ),
                """
                _##_
                ____
                ____
                ____
                """
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(4, { stone.down() })

            assertPositionArea(
                stone,
                Area(
                    Field(1, 2),
                    Field(2, 2),
                    Field(1, 3),
                    Field(2, 3)
                ),
                """
                ____
                ____
                _##_
                _##_
                """
            )
            assertTrue(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(10, { stone.down() })

            assertPositionArea(
                stone,
                Area("""
                    ___
                    ___
                    _##
                    _##
                """),
                """
                ____
                ____
                _##_
                _##_
                """
            )
            assertTrue(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `lands on debris`() {
            repeat(3, { stone.down() })

            assertPositionArea(
                stone,
                Area("""
                ___
                _##
                _##
                """),
                """
                ____
                _##_
                _##_
                ____
                """
            )

            val debris = Debris("""
                ____
                ____
                ____
                ####
            """
            )
            assertTrue(stone.landed(debris))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris4x4)

            assertPositionArea(
                stone,
                Area(
                    Field(0, -1),
                    Field(1, -1),
                    Field(0, 0),
                    Field(1, 0)
                ),
                """
                ##__
                ____
                ____
                ____
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris4x4)
            stone.left(debris4x4)

            assertPositionArea(
                stone,
                Area(
                    Field(0, -1),
                    Field(1, -1),
                    Field(0, 0),
                    Field(1, 0)
                ),
                """
                ##__
                ____
                ____
                ____
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris4x4)

            assertPositionArea(
                stone,
                Area(
                    Field(2, -1),
                    Field(3, -1),
                    Field(2, 0),
                    Field(3, 0)
                ),
                """
                __##
                ____
                ____
                ____
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris4x4)
            stone.right(debris4x4)

            assertPositionArea(
                stone,
                Area(
                    Field(2, -1),
                    Field(3, -1),
                    Field(2, 0),
                    Field(3, 0)
                ),
                """
                __##
                ____
                ____
                ____
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }
    }

    @Nested
    inner class `with a size 3and1` {

        @Disabled
        @Test
        fun rotates() {
            val stone = Stone(
                Structure("""
                #_
                ##
                #_
                """),
                Frame(4, 4)
            )

            stone.down()
            stone.down()
            stone.rotate()

            assertState(
                """
                ###_
                _#__
                ____
                ____
                """,
                stone
            )
        }
    }

    private fun assertPositionArea(stone: Stone, area: Area, state: String) {
        assertState(state, stone)
        area.fields.forEach { field -> assertTrue(stone.isAt(field)) }
    }

    private fun assertPosition(stone: Stone, field: Field, state: String) {
        assertPosition(stone, listOf(field), state)
    }

    private fun assertPosition(stone: Stone, fields: List<Field>, state: String) {
        assertState(state, stone)
        fields.forEach { field -> assertTrue(stone.isAt(field)) }
    }

    private fun assertState(state: String, stone: Stone) {
        assertEquals(
            "\n" + state.trimIndent() + "\n",
            "\n" + Tetris.draw(stone.state()) + "\n"
        )
    }
}