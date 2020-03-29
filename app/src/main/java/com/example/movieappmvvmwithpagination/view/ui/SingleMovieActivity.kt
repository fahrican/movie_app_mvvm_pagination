package com.example.movieappmvvmwithpagination.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movieappmvvmwithpagination.R
import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.constant.POSTER_BASE_URL
import com.example.movieappmvvmwithpagination.data.repository.MovieDetailsRepository
import com.example.movieappmvvmwithpagination.data.repository.NetworkState
import com.example.movieappmvvmwithpagination.data.vo.MovieDetails
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var singleMovieViewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: ITheMovieDB = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)

        singleMovieViewModel = getViewModel(movieId)
        singleMovieViewModel.movieDetails.observe(this, Observer {// todo: create separate method
            bindUI(it)
        })

        singleMovieViewModel.networkSate.observe(this, Observer {// todo: create separate method
            single_movie_progressbar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE

            single_movie_error_text.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
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

    private fun bindUI(movieDetails: MovieDetails) { // todo: use data binding
        single_movie_title.text = movieDetails.title
        single_movie_sub_title.text = movieDetails.tagline
        single_movie_info.text = movieDetails.overview
        single_movie_release_date.text = movieDetails.releaseDate

        val moviePosterURL: String = POSTER_BASE_URL + movieDetails.posterPath
        Glide.with(this).load(moviePosterURL).into(single_movie_poster)
    }
}
