package com.example.triviaapp.data


//wrapper class for emitting metadata
data class DataOrException<T, Boolean, E: Exception>(
    var data : T? = null,
    var loading : Boolean? = null,
    var e: E? = null
)
