package com.example.movieappmvvmwithpagination.data.api

import com.example.movieappmvvmwithpagination.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ITheMovieDB {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}