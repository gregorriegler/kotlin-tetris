class Timer(
    private val consumer: () -> Unit,
    private var tickEvery: Long = 250
) {

    private var lastTick: Long = 0

    fun time(time: Long) {
        if (time - lastTick >= tickEvery) {
            lastTick = time
            consumer.invoke()
        }
    }

    fun speed() {
        tickEvery = 50
    }

    fun normal() {
        tickEvery = 250
    }

}