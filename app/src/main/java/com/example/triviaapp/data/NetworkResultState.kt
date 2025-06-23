package com.example.triviaapp.data

sealed class NetworkResultState<out R> private constructor() {
    data class Success<out T>(val data: T) : NetworkResultState<T>()
    data class Error(val error: String) : NetworkResultState<Nothing>()
    data object Loading : NetworkResultState<Nothing>()
}