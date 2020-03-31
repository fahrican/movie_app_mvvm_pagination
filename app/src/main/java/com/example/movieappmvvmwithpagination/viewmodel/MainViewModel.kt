package com.example.movieappmvvmwithpagination.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    val networkState: LiveData<NetworkState> by lazy {
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

class MainViewModelFactory(
    private val movieRepository: MoviePagedListRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(movieRepository) as T
    }
}