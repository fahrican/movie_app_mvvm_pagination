package com.example.movieappmvvmwithpagination.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.movieappmvvmwithpagination.R
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.constant.INTENT_ID
import com.example.movieappmvvmwithpagination.data.repository.MovieDetailsRepository
import com.example.movieappmvvmwithpagination.data.status.NetworkState
import com.example.movieappmvvmwithpagination.databinding.ActivitySingleMovieBinding
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModel

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var singleMovieViewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    private lateinit var activitySingleMovieBinding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySingleMovieBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra(INTENT_ID, 1)
        val apiService: ITheMovieDB = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)
        singleMovieViewModel = getViewModel(movieId)

        observeLiveData()
    }

    private fun observeLiveData() {
        observeMovieDetails()
        observeNetworkState()
    }

    private fun observeNetworkState() {
        singleMovieViewModel.networkState.observe(this, Observer {
            activitySingleMovieBinding.singleMovieProgressbar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE

            activitySingleMovieBinding.singleMovieErrorText.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun observeMovieDetails() {
        singleMovieViewModel.movieDetails.observe(this, Observer {
            activitySingleMovieBinding.md = it
        })
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel { // todo: use viewModel()
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieDetailsRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
