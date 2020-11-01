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
            assertPosition(stone, Field.filled(1, -1))
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPosition(stone, Field.filled(1, 0))
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(3, { stone.down() })

            assertPosition(stone, Field.filled(1, 2))
            assertTrue(stone.landed(debris3x3.grid))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(4, { stone.down() })

            assertPosition(stone, Field.filled(1, 2))
            val obstacle = Debris(frame)
            assertTrue(stone.landed(obstacle.grid))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris3x3.grid)

            assertPosition(stone, Field.filled(0, 0))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris3x3.grid)
            stone.left(debris3x3.grid)

            assertPosition(stone, Field.filled(0, 0))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris3x3.grid)

            assertPosition(stone, Field.filled(2, 0))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris3x3.grid)
            stone.right(debris3x3.grid)

            assertPosition(stone, Field.filled(2, 0))
        }
    }

    @Nested
    inner class `with a size 2x2` {
        val stone = Stone(Structure.createDot(), TetrisFrame(4, 4))

        @Test
        fun `starts above the center`() {
            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(1, -2),
                    Field.filled(2, -2),
                    Field.filled(1, -1),
                    Field.filled(2, -1)
                )
            )
        }

        @Test
        fun `can be moved down`() {
            stone.down()

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(1, -1),
                    Field.filled(2, -1),
                    Field.filled(1, -0),
                    Field.filled(2, -0)
                )
            )
        }

        @Test
        fun `can be moved to the bottom`() {
            repeat(4, { stone.down() })

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(1, 2),
                    Field.filled(2, 2),
                    Field.filled(1, 3),
                    Field.filled(2, 3)
                )
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertTrue(stone.landed(obstacle.grid))
        }

        @Test
        fun `can not be moved out of the frame at the bottom`() {
            repeat(10, { stone.down() })

            assertPositionByGrid(
                stone,
                Grid("""
                    >>>
                    >>>
                    >##
                    >##
                """)
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertTrue(stone.landed(obstacle.grid))
        }

        @Test
        fun `lands on debris`() {
            repeat(3, { stone.down() })

            assertPositionByGrid(
                stone,
                Grid("""
                >>>
                >##
                >##
                """)
            )

            val obstacle = Debris(
                """
                ----
                ----
                ----
                ####
            """
            )
            assertTrue(stone.landed(obstacle.grid))
        }

        @Test
        fun `can be moved to the left`() {
            stone.down()
            stone.left(debris4x4.grid)

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(0, -1),
                    Field.filled(1, -1),
                    Field.filled(0, 0),
                    Field.filled(1, 0)
                )
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertFalse(stone.landed(obstacle.grid))
        }

        @Test
        fun `can not be moved out of the frame at the left`() {
            stone.down()
            stone.left(debris4x4.grid)
            stone.left(debris4x4.grid)

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(0, -1),
                    Field.filled(1, -1),
                    Field.filled(0, 0),
                    Field.filled(1, 0)
                )
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertFalse(stone.landed(obstacle.grid))
        }

        @Test
        fun `can be moved to the right`() {
            stone.down()
            stone.right(debris4x4.grid)

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(2, -1),
                    Field.filled(3, -1),
                    Field.filled(2, 0),
                    Field.filled(3, 0)
                )
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertFalse(stone.landed(obstacle.grid))
        }

        @Test
        fun `can not be moved out of the frame at the right`() {
            stone.down()
            stone.right(debris4x4.grid)
            stone.right(debris4x4.grid)

            assertPositionByGrid(
                stone,
                Grid(
                    Field.filled(2, -1),
                    Field.filled(3, -1),
                    Field.filled(2, 0),
                    Field.filled(3, 0)
                )
            )
            val obstacle = Debris(TetrisFrame(4, 4))
            assertFalse(stone.landed(obstacle.grid))
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
            val obstacle = Debris(frame)
            stone.rotate(obstacle.grid)

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
            val obstacle = Debris(frame)
            stone.right(obstacle.grid)
            val obstacle1 = Debris(frame)
            stone.rotate(obstacle1.grid)

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
            val obstacle = Debris(frame)
            stone.left(obstacle.grid)
            val obstacle1 = Debris(frame)
            stone.rotate(obstacle1.grid)

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
            val obstacle = Debris(frame)
            stone.rotate(obstacle.grid)

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
            stone.rotate(debris.grid)

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
            stone.right(debris.grid)
            stone.rotate(debris.grid)

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
            stone.right(debris.grid)
            stone.right(debris.grid)
            stone.rotate(debris.grid)

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
        stone.left(debris.grid)
        stone.left(debris.grid)
        stone.rotate(debris.grid)

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
        assertPositionByGrid(stone, Grid(field))
    }

    private fun assertPositionByGrid(stone: Stone, grid: Grid) {
        assertThat(stone.grid).isEqualTo(grid)
    }

    private fun assertState(state: String, stone: Stone) {
        assertEquals(
            "\n" + state.trimIndent() + "\n",
            stone.toString()
        )
    }
}