class EmptyField(x: Int, y: Int) : Field(x, y) {

    override fun isFilled(): Boolean = false

    override fun below(): Field {
        return super.below().empty()
    }

    override fun toTheLeft(): Field {
        return super.toTheLeft().empty()
    }

    override fun toTheRight(): Field {
        return super.toTheRight().empty()
    }

    override fun plus(field: Field): Field {
        return super.plus(field).empty()
    }

    override fun minus(field: Field): Field {
        return super.minus(field).empty()
    }

    override fun rotate(width: Int): Field {
        return super.rotate(width).empty()
    }

    override fun toString(): String = "EmptyField($x,$y)"
}