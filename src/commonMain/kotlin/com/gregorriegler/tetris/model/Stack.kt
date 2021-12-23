package com.gregorriegler.tetris.model

class Stack(
    private val fields: List<Field>
) {

    fun fall() = Stack(fields.map(Field::fall))

    fun toList(): List<Field> = fields

    fun moveEmptyAbove() = this.fieldBelow().upBy(fields.size)

    private fun fieldBelow() = Field.empty(fields[0].x, fields[0].y + 1)

}