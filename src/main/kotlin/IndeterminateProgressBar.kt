import kotlinx.coroutines.*

interface IIndeterminateProgressBar {
    suspend fun start()
    fun stop(doneMessage: String = "Done!")
}

class IndeterminateProgressBar(
    private var prefix: String = "",
    private var suffix: String = "",
    private val bordered: Char = '▱',
    private val filled: Char = '▰',
    private val length: Short = 10,
) : IIndeterminateProgressBar {

    private val progress: String
        get() {
            var progress = ""
            for (i in 0 until length) {
                progress += bordered
            }
            return progress
        }

    init {
        when {
            length < 3 -> throw IllegalArgumentException("Param length can't be less than 3, current length is $length")
            length > 10 -> throw IllegalArgumentException("Param length can't be more than 10, current length is $length")
        }
    }

    private var elapsedTime = 0L
    private var job: Job? = null

    @Suppress("NAME_SHADOWING")
    override suspend fun start() {
        val startTime = System.currentTimeMillis()
        job = CoroutineScope(Dispatchers.IO).launch {
            this@IndeterminateProgressBar.apply {
                while (true) {
                    for (i in 0 until length) {
                        elapsedTime = System.currentTimeMillis() - startTime
                        val progress = progress.replace(i, filled)
                        val suffix =
                            if (suffix.isNotEmpty()) "$suffix elapsed time ${elapsedTime}ms" else "elapsed time ${elapsedTime}ms"
                        val data = "\r$prefix $progress $suffix"
                        print(data = data)
                        delay(300L)
                    }
                }
            }
        }
    }

    override fun stop(doneMessage: String) {
        job?.cancel()
        job = null
        val progress = progress.replace(bordered, filled)
        val suffix =
            if (suffix.isNotEmpty()) "$suffix elapsed time ${elapsedTime}ms" else "elapsed time ${elapsedTime}ms"
        elapsedTime = 0L
        val data = "\r$prefix $progress $suffix\r\n"
        print(data = data)
    }

    fun updatePrefix(newPrefix: String) {
        prefix = newPrefix
    }

    fun updateSuffix(newSuffix: String) {
        suffix = newSuffix
    }

    private fun print(data: String) {
        System.out.write(data.toByteArray())
        System.out.flush()
    }

    private fun String.replace(index: Int, char: Char): String {
        val chars = toCharArray()
        chars[index] = char
        return String(chars)
    }
}