package br.com.busdataapplication.network

import br.com.busdataapplication.network.responses.AllVehiclesResponse
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class NetworkConnection {

    companion object {
        val token: String = "fdb12f8567b3420f85ddb4f6a446083ae79eff094a7cb66508848e63887d70eb"
        val baseUrl: String = "https://api.olhovivo.sptrans.com.br/v2.1/"

        fun retrofit(): Retrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }

        private fun client(): OkHttpClient {
            val client = OkHttpClient.Builder()
                .addInterceptor {
                    val newRequest: Request = it.request().newBuilder()
                        .addHeader("token", token)
                        .build()
                    it.proceed(newRequest)
                }
                .cookieJar(CustomCookieJar())
                .build()
            return client
        }
    }
}

class CustomCookieJar : CookieJar {
    private var cookies: List<Cookie> = listOf()

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {
        this.cookies = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return this.cookies
    }
}

interface OlhoVivoService {

    @POST("Login/Autenticar")
    fun authenticate(
        @Query("token") token: String
    ): Call<String>

    @GET("Posicao")
    fun getVehiclesPosition(
    ): Call<AllVehiclesResponse>
}