package com.gregorriegler.tetris.model

class AnchorFinder(val grid: Grid) {

    private val checked: HashSet<Position> = HashSet()

    fun hasAnchor(position: Position): Boolean {
        if (checked.contains(position)) return false
        checked.add(position)
        return falls(position)
            && (isAnchor(position)
            || hasAnchor(position.up())
            || hasAnchor(position.down())
            || hasAnchor(position.right())
            || hasAnchor(position.left())
            )
    }

    private fun falls(position: Position) = grid.get(position).falls()
    private fun isAnchor(position: Position) = standsOnSoil(position) || standsOnBottom(position)
    private fun standsOnSoil(position: Position) = grid.below(position).isSoilOrCoin()
    private fun standsOnBottom(position: Position) = position.y == grid.bottom
}