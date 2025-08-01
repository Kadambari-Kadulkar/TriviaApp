package com.example.triviaapp.network

import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.data.NetworkResultState
import com.example.triviaapp.model.Questions
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface QusetionApi {
//    @GET("/my/api/call")
//    suspend fun getSearchProduct(@Query("p1") p1: String , @Query("p2") p2: String )
    @GET("/api.php?")
    suspend fun getAllQuestions(@Query("amount") amount: Int ,
                                @Query("category") category: Int ,
                                @Query("difficulty") difficulty: String ) : Questions
}