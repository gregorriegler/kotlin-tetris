class GameClock(
    private val tickEvery: Long,
    private val consumer: () -> Unit
) {

    var lastTick: Long = 0

    fun time(time: Long) {
        if (time - lastTick >= tickEvery) {
            lastTick = time
            consumer.invoke()
        }
    }

}