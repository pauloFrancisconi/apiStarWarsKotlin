package com.example.composecharactersbase.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.security.cert.X509Certificate
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.util.concurrent.TimeUnit

// Função para configurar o OkHttpClient para ignorar a verificação de SSL
fun getUnsafeOkHttpClient(): OkHttpClient {
    try {
        // Criando um TrustManager que não faz verificação de certificados
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                // Retorna uma lista vazia, mas isso é aceito para evitar o erro.
                return arrayOf()
            }

            override fun checkClientTrusted(certificates: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certificates: Array<X509Certificate>, authType: String) {}
        })

        // Configurando o SSLContext com o TrustManager que aceita qualquer certificado
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory
        val trustManager = trustAllCertificates[0] as X509TrustManager

        // Retornando o OkHttpClient configurado
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)  // Passando o trustManager corretamente
            .hostnameVerifier { _, _ -> true }  // Ignora a verificação do hostname
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    } catch (e: Exception) {
        throw RuntimeException("Erro ao criar cliente OkHttp que ignora SSL", e)
    }
}

interface StarWarsApiService {
    @GET("films/")
    suspend fun getFilms(): FilmResponse

    companion object {
        private const val BASE_URL = "https://swapi.dev/api/"

        fun create(): StarWarsApiService {

            val client = getUnsafeOkHttpClient()


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StarWarsApiService::class.java)
        }
    }
}
