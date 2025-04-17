package com.example.composecharactersbase.network

data class FilmResponse(
    val results: List<Film>
)

data class Film(
    val title: String,
    val director: String,
    val producer: String,
    val release_date: String
)
