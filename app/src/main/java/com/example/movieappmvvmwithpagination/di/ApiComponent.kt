package com.example.movieappmvvmwithpagination.di

import com.example.movieappmvvmwithpagination.view.ui.SingleMovieActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(singleMovieActivity: SingleMovieActivity)
}