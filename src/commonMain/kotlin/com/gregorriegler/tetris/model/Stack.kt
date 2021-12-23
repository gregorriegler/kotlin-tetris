package com.gregorriegler.tetris.model

class Stack(
    private val fields: List<Field>
) {

    fun toList(): List<Field> = fields

    fun contains(it: Field) = fields.contains(it)

    fun down() = Stack(fields.map { it.down() })

    fun emptyUp() = this.emptyBelow().upBy(this.fields.size)

    private fun emptyBelow() = Field.empty(fields[0].x, fields[0].y + 1)

}