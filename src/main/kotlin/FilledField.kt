class FilledField(x: Int, y: Int) : Field(x, y) {

    override fun isFilled(): Boolean = true

    // todo need to find a better design to get rid of these duplicates
    override fun below(): Field {
        return super.below().filled()
    }

    override fun toTheLeft(): Field {
        return super.toTheLeft().filled()
    }

    override fun toTheRight(): Field {
        return super.toTheRight().filled()
    }

    override fun plus(field: Field): Field {
        return super.plus(field).filled()
    }

    override fun minus(field: Field): Field {
        return super.minus(field).filled()
    }

    override fun rotate(width: Int): Field {
        return super.rotate(width).filled()
    }

    override fun toString(): String = "FilledField($x,$y)"
}