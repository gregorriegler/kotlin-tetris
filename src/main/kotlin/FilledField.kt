class FilledField(x: Int, y: Int) : Field(x, y) {

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

    override fun toString(): String = "FilledField($x,$y)"
}