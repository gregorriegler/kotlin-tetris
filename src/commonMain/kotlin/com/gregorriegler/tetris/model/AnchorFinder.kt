package com.gregorriegler.tetris.model

class AnchorFinder(val area: Area) {

    private val checked: HashSet<MovablePosition> = HashSet()

    fun hasAnchor(position: MovablePosition): Boolean {
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

    private fun isAnchor(position: MovablePosition) = standsOnSoil(position) || standsOnBottom(position)
    private fun standsOnSoil(position: MovablePosition) = area.below(position).isSoil()
    private fun standsOnBottom(position: MovablePosition) = position.y == area.bottom
}