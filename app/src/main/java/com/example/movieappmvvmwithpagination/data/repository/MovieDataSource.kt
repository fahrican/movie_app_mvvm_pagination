package com.example.movieappmvvmwithpagination.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieappmvvmwithpagination.data.api.FIRST_PAGE
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val apiService: ITheMovieDB,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE
    val networkSate = MutableLiveData<NetworkSate>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkSate.postValue(NetworkSate.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList, null, page + 1)
                        networkSate.postValue(NetworkSate.LOADED)
                    },
                    {
                        networkSate.postValue(NetworkSate.ERROR)
                        Log.e("MovieDataSource", "${it.message}")
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.movieList, params.key + 1)
                            networkSate.postValue(NetworkSate.LOADED)
                        } else {
                            networkSate.postValue(NetworkSate.END_OF_LIST)
                        }
                    },
                    {
                        networkSate.postValue(NetworkSate.ERROR)
                        Log.e("MovieDataSource", "${it.message}")
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }
}