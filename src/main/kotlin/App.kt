import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val api = Api()
    val parser = Parser()
    val repository = Repository(api, parser)
    val availableCountries = Country.values().map { mapOf(it.ordinal to it.name) }

    print("Country (Available ${availableCountries}): ")
    val country = when (readLine() ?: "0") {
        "0" -> Country.USA
        "1" -> Country.Russia
        "2" -> Country.Korea
        else -> Country.USA
    }

    print("First page (Default 1): ")
    val firstPage = readLine()?.toInt() ?: 1

    print("Last page (Default 1): ")
    val lastPage = readLine()?.toInt() ?: 1

    print(
        """
        Selected country: $country
        First page: $firstPage
        Last page: $lastPage
        
    """.trimIndent()
    )

    GlobalScope.launch {
        val elapsedTime = measureTimeMillis {
            val ids = repository.parseIds(country, firstPage..lastPage, 500L)
            val carsDetails = repository.parseDetails(ids, 1_000L)

            val rangeStr = "range=$firstPage..$lastPage"
            val sizeStr = "size=${if (lastPage - firstPage == 0) 20 else (lastPage - firstPage) * 20}"
            repository.writeToCSV("$country($rangeStr, $sizeStr)", carsDetails)
        }
        println("Done! Took ${elapsedTime}ms (${elapsedTime / 1000} second(s)). Now, you can close this console.")
    }

    readLine()
}



