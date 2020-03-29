package com.example.movieappmvvmwithpagination.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieappmvvmwithpagination.R
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.repository.MoviePagedListRepository
import com.example.movieappmvvmwithpagination.data.status.NetworkState
import com.example.movieappmvvmwithpagination.view.adapter.PopularMoviePagedListAdapter
import com.example.movieappmvvmwithpagination.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: ITheMovieDB = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = callbackGetSpanSizeLookup(movieAdapter)

        setUpRecyclerView(gridLayoutManager, movieAdapter)

        observeLiveData(movieAdapter)
    }

    private fun callbackGetSpanSizeLookup(movieAdapter: PopularMoviePagedListAdapter): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                // MOVIE_VIEW_TYPE will occupy 1 out of 3 span
                // NETWORK_VIEW_TYPE will occupy all 3 span
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1 else 3
            }
        }
    }

    private fun setUpRecyclerView(gridLayoutManager: GridLayoutManager, movieAdapter: PopularMoviePagedListAdapter) {
        main_rv.layoutManager = gridLayoutManager
        main_rv.setHasFixedSize(true)
        main_rv.adapter = movieAdapter
    }

    private fun observeLiveData(movieAdapter: PopularMoviePagedListAdapter) {
        observeMoviePagedList(movieAdapter)
        observeNetworkState(movieAdapter)
    }

    private fun observeNetworkState(movieAdapter: PopularMoviePagedListAdapter) {
        viewModel.networkState.observe(this, Observer {
            main_progressbar.visibility =
                if (viewModel.checkIsListEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            main_movie_error_text.visibility =
                if (viewModel.checkIsListEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.checkIsListEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun observeMoviePagedList(movieAdapter: PopularMoviePagedListAdapter) {
        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })
    }

    private fun getViewModel(): MainViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory { // todo: use viewModel()
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(movieRepository) as T
            }
        })[MainViewModel::class.java]
    }
}
