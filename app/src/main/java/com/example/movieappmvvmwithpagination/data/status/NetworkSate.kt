package com.example.movieappmvvmwithpagination.data.status

import com.example.movieappmvvmwithpagination.data.constant.ERROR_TEXT
import com.example.movieappmvvmwithpagination.data.constant.REACHED_END_TEXT
import com.example.movieappmvvmwithpagination.data.constant.RUNNING_TEXT
import com.example.movieappmvvmwithpagination.data.constant.SUCCESS_TEXT

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        val LOADED = NetworkState(Status.SUCCESS, SUCCESS_TEXT)
        val LOADING = NetworkState(Status.RUNNING, RUNNING_TEXT)
        val ERROR = NetworkState(Status.FAILED, ERROR_TEXT)
        val END_OF_LIST = NetworkState(Status.FAILED, REACHED_END_TEXT)
    }
}