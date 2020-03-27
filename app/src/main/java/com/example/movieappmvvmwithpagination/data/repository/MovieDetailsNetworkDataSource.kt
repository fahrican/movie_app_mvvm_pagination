package com.example.movieappmvvmwithpagination.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(
    private val apiService: ITheMovieDB,
    private val compositeDisposable: CompositeDisposable
) {

    private val networkSate = MutableLiveData<NetworkSate>()
    val networkSateLD: LiveData<NetworkSate>
        get() = networkSate

    private val downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponseLD: LiveData<MovieDetails>
        get() = downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        networkSate.postValue(NetworkSate.LOADING)
        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            downloadedMovieDetailsResponse.postValue(it)
                            networkSate.postValue(NetworkSate.LOADED)
                        },
                        {
                            networkSate.postValue(NetworkSate.ERROR)
                            Log.e("MovieDetailsNetworkDS", "${it.message}")
                        })
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsNetworkDS", "${e.message}")
        }
    }
}