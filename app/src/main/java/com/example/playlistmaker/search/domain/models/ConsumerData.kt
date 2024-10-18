package com.example.playlistmaker.search.domain.models

sealed interface ConsumerData<T> {
    data class Data<T>(val data: T): ConsumerData<T>
    data class Error<T>(val message: String): ConsumerData<T>
}