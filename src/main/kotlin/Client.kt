import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

val client = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = GsonSerializer {
            serializeNulls()
        }
    }

    engine {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        config {
            readTimeout(240, TimeUnit.SECONDS)
            connectTimeout(240, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
    }
}