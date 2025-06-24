package com.example.triviaapp.repository

import android.util.Log
import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.model.Questions
import com.example.triviaapp.network.QusetionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(val api: QusetionApi) {

    private val dataOrException = DataOrException<Questions,Boolean,Exception>()

    suspend fun getAllQuestions(): DataOrException<Questions, Boolean, Exception>
     {
        try{
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions("20","21","medium")
            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        }
        catch (ex: Exception){
            dataOrException.e = ex
            Log.e("Exception: ", "API:getAllQuestions: "+ dataOrException.e!!.localizedMessage)
        }
        return dataOrException
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