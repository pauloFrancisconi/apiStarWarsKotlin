package com.example.composecharactersbase.network

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface StarWarsApiService {
    @GET("films/")
    suspend fun getFilms(): FilmResponse

    companion object {
        private const val BASE_URL = "https://swapi.dev/api/"

        fun create(): StarWarsApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StarWarsApiService::class.java)
        }
    }
}
