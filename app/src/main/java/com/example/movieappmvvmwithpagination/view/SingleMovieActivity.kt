package com.example.movieappmvvmwithpagination.view

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
import com.example.movieappmvvmwithpagination.data.api.POSTER_BASE_URL
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.repository.MovieDetailsRepository
import com.example.movieappmvvmwithpagination.data.repository.NetworkSate
import com.example.movieappmvvmwithpagination.data.vo.MovieDetails
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var singleMovieViewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val apiService: ITheMovieDB = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)

        singleMovieViewModel = getViewModel(1)
        singleMovieViewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        singleMovieViewModel.networkSate.observe(this, Observer {
            single_movie_progressbar.visibility =
                if (it == NetworkSate.LOADING) View.VISIBLE else View.GONE

            single_movie_error_text.visibility =
                if (it == NetworkSate.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieDetailsRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    private fun bindUI(movieDetails: MovieDetails) {
        single_movie_title.text = movieDetails.title
        single_movie_sub_title.text = movieDetails.tagline
        single_movie_info.text = movieDetails.overview
        single_movie_release_date.text = movieDetails.releaseDate

        val moviePosterURL: String = POSTER_BASE_URL + movieDetails.posterPath
        Glide.with(this).load(moviePosterURL).into(single_movie_poster)
    }
}
