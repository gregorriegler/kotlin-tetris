package com.gregorriegler.tetris.model

class Stack(
    private val fields: List<Field>
) {

    fun toList(): List<Field> = fields

    fun contains(it: Field) = fields.contains(it)

    fun fall() = Stack(fields.map(Field::fall))

    fun emptyUp() = this.fieldBelow().upBy(fields.size)

    private fun fieldBelow() = Field.empty(fields[0].x, fields[0].y + 1)

}