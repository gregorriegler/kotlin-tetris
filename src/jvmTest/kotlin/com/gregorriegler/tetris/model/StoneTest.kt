package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class `A Stone` {

    val frame = TetrisFrame(3, 3)
    val debris3x3 = Debris(frame)
    val debris4x4 = Debris(TetrisFrame(4, 4))

    @Nested
    inner class `with the size of a single field` {
        val stone = Stone(Structure("#"), frame)

        @Test
        fun `starts above the center`() {
            assertPosition(stone, Field.filled(Position.of(1, -1)))
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPosition(stone, Field.filled(Position.of(1, 0)))
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(3, { stone.down() })

            assertPosition(stone, Field.filled(Position.of(1, 2)))
            assertTrue(stone.landed(debris3x3))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(4, { stone.down() })

            assertPosition(stone, Field.filled(Position.of(1, 2)))
            assertTrue(stone.landed(Debris(frame)))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris3x3)

            assertPosition(stone, Field.filled(Position.of(0, 0)))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris3x3)
            stone.left(debris3x3)

            assertPosition(stone, Field.filled(Position.of(0, 0)))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris3x3)

            assertPosition(stone, Field.filled(Position.of(2, 0)))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris3x3)
            stone.right(debris3x3)

            assertPosition(stone, Field.filled(Position.of(2, 0)))
        }
    }

    @Nested
    inner class `with a size 2x2` {
        val stone = Stone(Structure.createDot(), TetrisFrame(4, 4))

        @Test
        fun `starts above the center`() {
            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(1, -2)),
                    Field.filled(Position.of(2, -2)),
                    Field.filled(Position.of(1, -1)),
                    Field.filled(Position.of(2, -1))
                )
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(1, -1)),
                    Field.filled(Position.of(2, -1)),
                    Field.filled(Position.of(1, -0)),
                    Field.filled(Position.of(2, -0))
                )
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(4, { stone.down() })

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(1, 2)),
                    Field.filled(Position.of(2, 2)),
                    Field.filled(Position.of(1, 3)),
                    Field.filled(Position.of(2, 3))
                )
            )
            assertTrue(stone.landed(Debris(TetrisFrame(4, 4))))
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
                """)
            )
            assertTrue(stone.landed(Debris(TetrisFrame(4, 4))))
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
                """)
            )

            assertTrue(stone.landed(Debris("""
                ----
                ----
                ----
                ####
            """
            )))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(0, -1)),
                    Field.filled(Position.of(1, -1)),
                    Field.filled(Position.of(0, 0)),
                    Field.filled(Position.of(1, 0))
                )
            )
            assertFalse(stone.landed(Debris(TetrisFrame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris4x4)
            stone.left(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(0, -1)),
                    Field.filled(Position.of(1, -1)),
                    Field.filled(Position.of(0, 0)),
                    Field.filled(Position.of(1, 0))
                )
            )
            assertFalse(stone.landed(Debris(TetrisFrame(4, 4))))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(2, -1)),
                    Field.filled(Position.of(3, -1)),
                    Field.filled(Position.of(2, 0)),
                    Field.filled(Position.of(3, 0))
                )
            )
            assertFalse(stone.landed(Debris(TetrisFrame(4, 4))))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris4x4)
            stone.right(debris4x4)

            assertPositionByArea(
                stone,
                Area(
                    Field.filled(Position.of(2, -1)),
                    Field.filled(Position.of(3, -1)),
                    Field.filled(Position.of(2, 0)),
                    Field.filled(Position.of(3, 0))
                )
            )
            assertFalse(stone.landed(Debris(TetrisFrame(4, 4))))
        }
    }

    @Nested
    inner class `of shape T` {

        @Test
        fun rotates() {
            val frame = TetrisFrame(4, 4)
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
                ###
                -#-
                """,
                stone
            )
        }

        @Test
        fun `rotation respects right wall and moves back in`() {
            val frame = TetrisFrame(3, 3)
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
            val frame = TetrisFrame(3, 3)
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
            val frame = TetrisFrame(3, 3)
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
                ---
                """,
                stone
            )
        }

        @Test
        fun `rotation respects bottom debris`() {
            val frame = TetrisFrame(3, 3)
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

        @Test
        fun `rotation respects right debris and moves back in`() {
            val frame = TetrisFrame(4, 4)
            val stone = Stone("""
                -#-
                -#-
                -#-
                """,
                frame
            )
            val debris = Debris("""
                ---#
                ---#
                ---#
                ---#
                """
            )

            stone.down()
            stone.down()
            stone.down()
            stone.right(debris)
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

        @Test
        fun `rotation respects right debris and moves back in further`() {
            val frame = TetrisFrame(6, 6)
            val stone = Stone("""
                --#--
                --#--
                --#--
                --#--
                --#--
                """,
                frame
            )
            val debris = Debris("""
                -----#
                -----#
                -----#
                -----#
                -----#
                -----#
                """
            )

            repeat(5, {stone.down()})
            stone.right(debris)
            stone.right(debris)
            stone.rotate(debris)

            assertState(
                """
                -----
                -----
                #####
                -----
                -----
                """,
                stone
            )
        }
    }

    @Test
    fun `rotation respects left debris and moves back in`() {
        val stone = Stone("""
                --#--
                --#--
                --#--
                --#--
                --#--
                """,
            TetrisFrame(6, 6)
        )
        val debris = Debris("""
                #-----
                #-----
                #-----
                #-----
                #-----
                #-----
                """
        )

        repeat(5, {stone.down()})
        stone.left(debris)
        stone.left(debris)
        stone.rotate(debris)

        assertState(
            """
                ------
                ------
                -#####
                ------
                ------
                """,
            stone
        )
    }

    private fun assertPosition(stone: Stone, field: Field) {
        assertPositionByArea(stone, Area(field))
    }

    private fun assertPositionByArea(stone: Stone, area: Area) {
        assertThat(stone.area).isEqualTo(area)
    }

    private fun assertState(state: String, stone: Stone) {
        assertEquals(
            "\n" + state.trimIndent() + "\n",
            stone.toString()
        )
    }
}