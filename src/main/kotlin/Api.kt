import io.ktor.client.request.*

class Api {
    companion object {
        private const val baseUrl = "https://kolesa.kz"
    }

    suspend fun cars(country: Country, page: Int): String {
        return client.get(urlString = "$baseUrl/cars/?mark-country=${country.value}${if (page > 1) "&page=$page" else ""}")
    }

    suspend fun details(id: String): String {
        return client.get(urlString = "$baseUrl/a/show/$id")
    }
}