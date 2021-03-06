package com.gregorriegler.tetris.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class `A Field` {

    @Test
    fun `can create bomb`() {
        val bomb = Field.bomb(0, 3)
        assertThat(bomb.filling).isEqualTo(Filling.BOMB)
        assertThat(bomb.x).isEqualTo(0)
        assertThat(bomb.y).isEqualTo(3)
    }

    @Test
    fun `has a filling`() {
        assertThat(Field(1,1).filling).isEqualTo(Filling.EMPTY)
        assertThat(Field(1,1, Filling.FALLING).filling).isEqualTo(Filling.FALLING)
    }

    @Test
    fun `moves down`() {
        assertThat(Field(1,1).down()).isEqualTo(Field(1, 2))
    }

    @Test
    fun `moves down by 3`() {
        assertThat(Field(1,1).downBy(3)).isEqualTo(Field(1, 4))
    }

    @Test
    fun `moves to the left`() {
        assertThat(Field(1,1).left()).isEqualTo(Field(0, 1))
    }

    @Test
    fun `moves to the left by 3`() {
        assertThat(Field(1,1).leftBy(3)).isEqualTo(Field(-2, 1))
    }

    @Test
    fun `moves to the right`() {
        assertThat(Field(1,1).right()).isEqualTo(Field(2, 1))
    }

    @Test
    fun `moves to the right by 3`() {
        assertThat(Field(1,1).rightBy(3)).isEqualTo(Field(4, 1))
    }

    @Test
    fun `adds a position`() {
        assertThat(Field(1,1).plus(Position.of(1,1))).isEqualTo(Field(2, 2))
    }

    @Test
    fun `substracts a position`() {
        assertThat(Field(1,1).minus(Position.of(1,1))).isEqualTo(Field(0, 0))
    }

    @Test
    fun `rotates a field in a 3x3 block`() {
        //top row
        val grid3x3 = Grid("###\n###\n###")
        assertThat(Field(0,0).rotate(grid3x3)).isEqualTo(Field(2, 0))
        assertThat(Field(1,0).rotate(grid3x3)).isEqualTo(Field(2, 1))
        assertThat(Field(2,0).rotate(grid3x3)).isEqualTo(Field(2, 2))
        //center row
        assertThat(Field(0,1).rotate(grid3x3)).isEqualTo(Field(1, 0))
        assertThat(Field(1,1).rotate(grid3x3)).isEqualTo(Field(1, 1))
        assertThat(Field(2,1).rotate(grid3x3)).isEqualTo(Field(1, 2))
        //bottom row
        assertThat(Field(0,2).rotate(grid3x3)).isEqualTo(Field(0, 0))
        assertThat(Field(1,2).rotate(grid3x3)).isEqualTo(Field(0, 1))
        assertThat(Field(2,2).rotate(grid3x3)).isEqualTo(Field(0, 2))
    }

    @Test
    fun erases() {
        assertThat(Field.empty(0, 0).erase()).isEqualTo(FieldScore(Field.empty(0, 0), 0))
        assertThat(Field.filled(0, 0).erase()).isEqualTo(FieldScore(Field.empty(0, 0), 1))
        assertThat(Field.coin(0, 0).erase()).isEqualTo(FieldScore(Field.empty(0, 0), 10))
    }

    @Test
    fun compares() {
        assertThat(Field.filled(0, 0).compareTo(Field.filled(0, 0))).isEqualTo(0)
        assertThat(Field.filled(1, 0).compareTo(Field.filled(0, 0))).isGreaterThan(0)
        assertThat(Field.filled(0, 1).compareTo(Field.filled(0, 0))).isGreaterThan(0)
        assertThat(Field.filled(0, 0).compareTo(Field.filled(1, 0))).isLessThan(0)
        assertThat(Field.filled(0, 0).compareTo(Field.filled(0, 1))).isLessThan(0)
    }
}