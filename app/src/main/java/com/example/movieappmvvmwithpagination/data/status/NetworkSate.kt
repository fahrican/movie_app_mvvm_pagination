package com.example.movieappmvvmwithpagination.data.status

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something went wrong!")
        val END_OF_LIST: NetworkState = NetworkState(Status.FAILED, "You have reached the end.")
    }
}