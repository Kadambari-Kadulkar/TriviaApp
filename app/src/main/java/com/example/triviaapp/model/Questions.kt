package com.example.triviaapp.model

data class Questions(
    val response_code: Int,
    val results: List<Result>
)