package com.example.movieappmvvmwithpagination.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieappmvvmwithpagination.data.repository.MoviePagedListRepository
import com.example.movieappmvvmwithpagination.data.status.NetworkState
import com.example.movieappmvvmwithpagination.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkSate: LiveData<NetworkState> by lazy { // todo: rename property
        movieRepository.getNetworkState()
    }

    fun checkIsListEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}