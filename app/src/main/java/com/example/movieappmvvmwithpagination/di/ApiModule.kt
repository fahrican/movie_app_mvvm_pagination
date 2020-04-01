package com.example.movieappmvvmwithpagination.di

import com.example.movieappmvvmwithpagination.data.api.ITheMovieDB
import com.example.movieappmvvmwithpagination.data.api.TheMovieDBClient
import com.example.movieappmvvmwithpagination.data.repository.MovieDetailsRepository
import com.example.movieappmvvmwithpagination.data.repository.MoviePagedListRepository
import com.example.movieappmvvmwithpagination.viewmodel.MainViewModelFactory
import com.example.movieappmvvmwithpagination.viewmodel.SingleMovieViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApi(): ITheMovieDB = TheMovieDBClient.getClient()

    @Provides
    fun provideMovieDetailsRepository(apiService: ITheMovieDB) = MovieDetailsRepository(apiService)

    @Provides
    fun provideSingleMovieViewModelFactory(movieDetailsRepo: MovieDetailsRepository): SingleMovieViewModelFactory {
        return SingleMovieViewModelFactory(movieDetailsRepo, 1)
    }

    @Provides
    fun provideMoviePagedListRepository(apiService: ITheMovieDB) = MoviePagedListRepository(apiService)

    @Provides
    fun provideMainViewModelFactory(moviePagedListRepo: MoviePagedListRepository) = MainViewModelFactory(moviePagedListRepo)
}