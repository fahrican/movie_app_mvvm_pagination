package com.example.movieappmvvmwithpagination.data.vo

import com.squareup.moshi.Json

data class MovieResponse(
    val page: Int,
    @field:Json(name = "results")
    val movieList: List<Movie>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_pages")
    val totalResults: Int
)