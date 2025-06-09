package com.example.playlistmaker.search.domain.models

import kotlinx.coroutines.flow.Flow

sealed interface ConsumerData<out T> {
    data class Data<out T>(val data: T): ConsumerData<T>
    data class Error<T>(val message: String): ConsumerData<T>
}