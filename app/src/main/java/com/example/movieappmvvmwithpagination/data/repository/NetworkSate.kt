package com.example.movieappmvvmwithpagination.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) { // todo: move class in separate package status

    companion object { // todo: use joined assignment
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val END_OF_LIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "success")
            LOADING = NetworkState(Status.RUNNING, "running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong!")
            END_OF_LIST = NetworkState(Status.FAILED, "You have reached the end.")
        }
    }
}