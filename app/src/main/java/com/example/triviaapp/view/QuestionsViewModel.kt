package com.example.triviaapp.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.data.NetworkResultState
import com.example.triviaapp.model.Questions
import com.example.triviaapp.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repo: QuestionRepository) :ViewModel() {
//    lateinit var questionsResponse : ArrayList<Questions> //= emptyArray<Questions>()
//    private val _networkState = MutableStateFlow(NetworkResultState.Loading)
//    val questState = _networkState.asStateFlow()
    val data : MutableState<DataOrException<Questions, Boolean, Exception>> =
        mutableStateOf(DataOrException(null,true,Exception("")))

    init {
        getAllQuestions()
    }
    private fun getAllQuestions() = viewModelScope.launch(Dispatchers.IO) {
        data.value.loading = true
        data.value = repo.getAllQuestions()
        //Log.e("questions: ", data.value.data!!.results.size.toString())
        if(data.value.data.toString().isNotEmpty()) data.value.loading = false

    }

//    fun getAllQuestions1() = liveData(Dispatchers.IO) {it
//            emit(NetworkResultState.Loading)
//        try{
//            emit(NetworkResultState.Success (data = repo.getAllQuestions()))
//
//        } catch (e: Exception){
//            println(e.printStackTrace())
//            emit(NetworkResultState.Error (error = e.printStackTrace().toString()))
//
//        }
//    }


}