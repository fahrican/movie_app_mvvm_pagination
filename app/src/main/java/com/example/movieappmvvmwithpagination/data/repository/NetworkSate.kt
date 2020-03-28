package com.example.movieappmvvmwithpagination.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkSate(val status: Status, val msg: String) {

    companion object {
        val LOADED: NetworkSate
        val LOADING: NetworkSate
        val ERROR: NetworkSate
        val END_OF_LIST: NetworkSate

        init {
            LOADED = NetworkSate(Status.SUCCESS, "success")
            LOADING = NetworkSate(Status.RUNNING, "running")
            ERROR = NetworkSate(Status.FAILED, "Something went wrong!")
            END_OF_LIST = NetworkSate(Status.FAILED, "You have reached the end.")
        }
    }
}