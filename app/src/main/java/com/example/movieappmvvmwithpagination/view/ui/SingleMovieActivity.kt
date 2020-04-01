package com.example.movieappmvvmwithpagination.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.movieappmvvmwithpagination.R
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.constant.INTENT_ID
import com.example.movieappmvvmwithpagination.data.repository.MovieDetailsRepository
import com.example.movieappmvvmwithpagination.data.status.NetworkState
import com.example.movieappmvvmwithpagination.databinding.ActivitySingleMovieBinding
import com.example.movieappmvvmwithpagination.di.DaggerApiComponent
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModel
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModelFactory
import javax.inject.Inject

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var activitySingleMovieBinding: ActivitySingleMovieBinding

    @Inject
    lateinit var apiService: ITheMovieDB
    @Inject
    lateinit var movieDetailsRepository: MovieDetailsRepository
    @Inject
    lateinit var singleMovieVMFactory: SingleMovieViewModelFactory
    private val singleMovieVM: SingleMovieViewModel by viewModels{ singleMovieVMFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySingleMovieBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        DaggerApiComponent.create().inject(this)

        val movieId: Int = intent.getIntExtra(INTENT_ID, 1)
        singleMovieVM.movieId = movieId

        observeLiveData()
    }

    private fun observeLiveData() {
        observeMovieDetails()
        observeNetworkState()
    }

    private fun observeNetworkState() {
        singleMovieVM.networkState.observe(this, Observer {
            activitySingleMovieBinding.singleMovieProgressbar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE

            activitySingleMovieBinding.singleMovieErrorText.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun observeMovieDetails() {
        singleMovieVM.movieDetailsLD.observe(this, Observer {
            activitySingleMovieBinding.md = it
        })
    }
}
