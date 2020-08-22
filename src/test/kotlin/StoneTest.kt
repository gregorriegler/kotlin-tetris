import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
                Field.filled(1, -1),
                """
                ---
                ---
                ---
                """
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPosition(
                stone,
                Field.filled(1, 0),
                """
                -#-
                ---
                ---
                """
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(3, { stone.down() })

            assertPosition(
                stone,
                Field.filled(1, 2),
                """
                ---
                ---
                -#-
                """
            )
            assertTrue(stone.landed(debris3x3))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(4, { stone.down() })

            assertPosition(
                stone,
                Field.filled(1, 2),
                """
                ---
                ---
                -#-
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
                Field.filled(0, 0),
                """
                #--
                ---
                ---
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
                Field.filled(0, 0),
                """
                #--
                ---
                ---
                """
            )
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris3x3)

            assertPosition(
                stone,
                Field.filled(2, 0),
                """
                --#
                ---
                ---
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
                Field.filled(2, 0),
                """
                --#
                ---
                ---
                """
            )
        }
    }

    @Nested
    inner class `with a size 2x2` {
        val stone = Stone(Structure.create2by2(), Frame(4, 4))

        @Test
        fun `starts above the center`() {
            assertPositionByArea(
                stone,
                Area(
                    Field.filled(1, -2),
                    Field.filled(2, -2),
                    Field.filled(1, -1),
                    Field.filled(2, -1)
                ),
                """
                ----
                ----
                ----
                ----
                """
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(1, -1),
                    Field.filled(2, -1),
                    Field.filled(1, -0),
                    Field.filled(2, -0)
                ),
                """
                -##-
                ----
                ----
                ----
                """
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(4, { stone.down() })

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(1, 2),
                    Field.filled(2, 2),
                    Field.filled(1, 3),
                    Field.filled(2, 3)
                ),
                """
                ----
                ----
                -##-
                -##-
                """
            )
            assertTrue(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(10, { stone.down() })

            assertPositionByArea(
                stone,
                Area("""
                    >>>
                    >>>
                    >##
                    >##
                """),
                """
                ----
                ----
                -##-
                -##-
                """
            )
            assertTrue(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `lands on debris`() {
            repeat(3, { stone.down() })

            assertPositionByArea(
                stone,
                Area("""
                >>>
                >##
                >##
                """),
                """
                ----
                -##-
                -##-
                ----
                """
            )


            val debris = Debris("""
                ----
                ----
                ----
                ####
            """
            )
            assertTrue(stone.landed(debris))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(0, -1),
                    Field.filled(1, -1),
                    Field.filled(0, 0),
                    Field.filled(1, 0)
                ),
                """
                ##--
                ----
                ----
                ----
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris4x4)
            stone.left(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(0, -1),
                    Field.filled(1, -1),
                    Field.filled(0, 0),
                    Field.filled(1, 0)
                ),
                """
                ##--
                ----
                ----
                ----
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(2, -1),
                    Field.filled(3, -1),
                    Field.filled(2, 0),
                    Field.filled(3, 0)
                ),
                """
                --##
                ----
                ----
                ----
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris4x4)
            stone.right(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(2, -1),
                    Field.filled(3, -1),
                    Field.filled(2, 0),
                    Field.filled(3, 0)
                ),
                """
                --##
                ----
                ----
                ----
                """
            )
            assertFalse(stone.landed(Debris(Frame(4, 4))))
        }
    }

    @Nested
    inner class `with a size 3and1` {

        @Test
        fun rotates() {
            val frame = Frame(4, 4)
            val stone = Stone("""
                #-
                ##
                #-
                """,
                frame
            )

            stone.down()
            stone.down()
            stone.down()
            stone.rotate(Debris(frame))

            assertState(
                """
                -###
                --#-
                ----
                ----
                """,
                stone
            )
        }

        @Test
        fun `rotation respects right wall and moves back in`() {
            val frame = Frame(3, 3)
            val stone = Stone("""
                -#-
                -#-
                -#-
                """,
                frame
            )

            stone.down()
            stone.down()
            stone.down()
            stone.right(Debris(frame))
            stone.rotate(Debris(frame))

            assertState(
                """
                ---
                ###
                ---
                """,
                stone
            )
        }

        @Test
        fun `rotation respects left wall and moves back in`() {
            val frame = Frame(3, 3)
            val stone = Stone("""
                -#-
                -#-
                -#-
                """,
                frame
            )

            stone.down()
            stone.down()
            stone.down()
            stone.left(Debris(frame))
            stone.rotate(Debris(frame))

            assertState(
                """
                ---
                ###
                ---
                """,
                stone
            )
        }

        @Test
        fun `rotation respects bottom`() {
            val frame = Frame(3, 3)
            val stone = Stone("""
                ---
                ###
                ---
                """,
                frame
            )

            stone.down()
            stone.down()
            stone.down()
            stone.down()
            stone.rotate(Debris(frame))

            assertState(
                """
                ---
                ---
                ###
                """,
                stone
            )
        }

        @Test
        fun `rotation respects bottom debris`() {
            val frame = Frame(3, 3)
            val stone = Stone("""
                ---
                ###
                ---
                """,
                frame
            )
            val debris = Debris("""
                ---
                ---
                ###
                """
            )

            stone.down()
            stone.down()
            stone.down()
            stone.rotate(debris)

            assertState(
                """
                ---
                ###
                ---
                """,
                stone
            )
        }
    }

    private fun assertPosition(stone: Stone, field: Field, state: String) {
        assertState(state, stone)
        assertThat(stone.area).isEqualTo(Area(field))
    }

    private fun assertPositionByArea(stone: Stone, area: Area, state: String) {
        assertState(state, stone)
        assertThat(stone.area).isEqualTo(area)
    }

    private fun assertState(state: String, stone: Stone) {
        assertEquals(
            "\n" + state.trimIndent() + "\n",
            "\n" + Area.draw(stone.state()) + "\n"
        )
    }
}