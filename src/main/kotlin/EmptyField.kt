class EmptyField(x: Int, y: Int) : Field(x, y) {

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

    override fun toString(): String = "EmptyField($x,$y)"
}