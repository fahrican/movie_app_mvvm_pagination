package com.example.movieappmvvmwithpagination.data.model

import com.squareup.moshi.Json

data class Movie(
    val id: Int,
    @field:Json(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "release_date")
    val releaseDate: String,
    val title: String
)