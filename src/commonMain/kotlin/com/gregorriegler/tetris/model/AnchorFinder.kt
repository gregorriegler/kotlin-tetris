package com.gregorriegler.tetris.model

class AnchorFinder(val area: Area) {

    fun hasAnchor(position: Position): Boolean =
        isAnchor(position)
                || hasAnchorToTheRight(area.rightOf(position))
                || hasAnchorToTheLeft(area.leftOf(position))

    private fun hasAnchorToTheRight(field: Field): Boolean =
        field.falls() && (isAnchor(field.position) || hasAnchorToTheRight(area.below(field.position)) || hasAnchorToTheRight(
            area.rightOf(field.position)
        ))

    private fun hasAnchorToTheLeft(field: Field): Boolean =
        field.falls() && (isAnchor(field.position) || hasAnchorToTheLeft(area.below(field.position)) || hasAnchorToTheLeft(
            area.leftOf(field.position)
        ))

    private fun isAnchor(position: Position) = standsOnSoil(position) || standsOnBottom(position)
    private fun standsOnSoil(position: Position) = area.below(position).isSoil()
    private fun standsOnBottom(position: Position) = position.y == area.bottom
}