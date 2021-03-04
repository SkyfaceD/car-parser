import io.ktor.client.request.*
import io.ktor.http.*

const val baseUrl = "https://kolesa.kz"

interface IApi {
    suspend fun cars(country: Country, page: Int): String
    suspend fun details(id: String): String
}

class Api : IApi {
    override suspend fun cars(country: Country, page: Int): String {
        return client.get(urlString = "$baseUrl/cars/?mark-country=${country.value}${if (page > 1) "&page=$page" else ""}")
    }

    override suspend fun details(id: String): String {
        return client.get(urlString = "$baseUrl/a/show/$id")
    }

    private fun String.asUrl() = Url(this)
}