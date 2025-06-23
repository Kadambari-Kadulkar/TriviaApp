package com.example.triviaapp.repository

import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.data.NetworkResultState
import com.example.triviaapp.model.Questions
import com.example.triviaapp.network.QusetionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(val api: QusetionApi) {

    private val listOfQuestions = DataOrException<ArrayList<Questions>,Boolean,Exception>()

    suspend fun getAllQuestions(): NetworkResultState<ArrayList<Questions>>
     {
        return api.getAllQuestions()

    }

    companion object {
        @Volatile
        private var instance: QuestionRepository? = null

        fun getInstance(apiService: QusetionApi): QuestionRepository =
            instance ?: synchronized(this) {
                instance ?: QuestionRepository(apiService)
            }.also { instance = it }
    }

}