package com.example.triviaapp.model

data class Questions(
    val response_code: Int,
    var results: List<Result>
)