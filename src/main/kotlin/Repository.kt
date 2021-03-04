import kotlinx.coroutines.delay
import java.io.FileWriter
import java.nio.charset.Charset

class Repository(
    private val api: Api,
    private val parser: Parser,
) {
    private val ipb = IndeterminateProgressBar(length = 5)

    suspend fun parseIds(country: Country, range: IntRange, delayInMillis: Long = 500L): List<String> {
        ipb.updatePrefix("Fetching car ids")
        ipb.start()

        val ids = arrayListOf<String>()

        for (page in range) {
            delay(delayInMillis)

            val apiResult = api.cars(country, page)
            val parserResult = parser.ids(apiResult)

            ids.addAll(parserResult)
        }

        ipb.stop()

        return ids
    }

    suspend fun parseDetails(ids: List<String>, delayInMillis: Long = 1_500L): List<CarDetails> {
        ipb.updatePrefix("Fetching car details")
        ipb.start()

        val carsDetails = arrayListOf<CarDetails>()
        var iteration = 0

        try {
            ids.forEachIndexed { index, id ->
                iteration = index
                delay(delayInMillis)

                val apiResult = api.details(id)
                val parserResult = parser.details(apiResult)

                carsDetails.add(parserResult)
            }
        } catch (e: Exception) {
            throw Exception("Died on $iteration")
        }

        ipb.stop()

        return carsDetails
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun writeToCSV(filename: String, carsDetails: List<CarDetails>) {
        ipb.updatePrefix("Writing parsed data into file $filename.csv")
        ipb.start()

        val fileWriter = FileWriter("$filename.csv", Charset.forName("UTF-8"))
        fileWriter.appendLine(plainParameters())
        carsDetails.forEach { carDetails ->
            fileWriter.appendLine(carDetails.toString())
        }
        fileWriter.flush()
        fileWriter.close()

        ipb.stop()
    }
}