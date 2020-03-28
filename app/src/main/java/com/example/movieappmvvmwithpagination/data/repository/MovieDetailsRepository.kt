package com.example.movieappmvvmwithpagination.data.repository

import androidx.lifecycle.LiveData
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: ITheMovieDB) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {

        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponseLD
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkSate> {
        return movieDetailsNetworkDataSource.networkSateLD
    }
}