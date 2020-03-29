package com.example.movieappmvvmwithpagination.data.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.movieappmvvmwithpagination.data.constant.POSTER_BASE_URL
import com.squareup.moshi.Json

data class MovieDetails(
    val budget: Int,
    val id: Int,
    val overview: String,
    val popularity: Double,
    @field:Json(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @field:Json(name = "vote_average")
    val rating: Double
)

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, uri: String?) {
    val moviePosterURL: String = POSTER_BASE_URL + uri
    Glide.with(imageView.context).load(moviePosterURL).into(imageView)
}
