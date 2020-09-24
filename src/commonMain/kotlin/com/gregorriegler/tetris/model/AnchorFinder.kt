package com.gregorriegler.tetris.model

class AnchorFinder(val area: Area) {

    private val checked: HashSet<Position> = HashSet()

    fun hasAnchor(position: Position): Boolean {
        if (checked.contains(position)) return false
        checked.add(position)
        return area.get(position).falls() && (
                isAnchor(position)
                        || hasAnchor(position.up())
                        || hasAnchor(position.down())
                        || hasAnchor(position.right())
                        || hasAnchor(position.left())
                )
    }

    private fun isAnchor(position: Position) = standsOnSoil(position) || standsOnBottom(position)
    private fun standsOnSoil(position: Position) = area.below(position).isSoilOrCoin()
    private fun standsOnBottom(position: Position) = position.y == area.bottom
}