package com.gregorriegler.tetris.model

class AreaAsListOfList(private val fields: List<List<Field>>) {
    companion object {
        fun parseFields(string: String): List<List<Field>> = string.trimIndent()
            .split("\n")
            .mapIndexed { y, row ->
                val pulls = row.filter { it == Filling.PULL_VALUE }.count() * 2
                row.toCharArray()
                    .withIndex()
                    .filterNot { it.value == Filling.INDENT_VALUE || it.value == Filling.PULL_VALUE }
                    .map { Field(it.index - pulls, y, it.value) }
                    .toList()
            }.toList()
    }

    constructor(string: String) : this(parseFields(string))

    fun get(x: Int, y: Int): Field {
        return fields[y][x]
    }

}
