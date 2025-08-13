package com.example.triviaapp.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.data.DataOrException
import com.example.triviaapp.model.Questions
import com.example.triviaapp.model.Result
import com.example.triviaapp.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repo: QuestionRepository) :ViewModel() {
//    lateinit var questionsResponse : ArrayList<Questions> //= emptyArray<Questions>()
//    private val _networkState = MutableStateFlow(NetworkResultState.Loading)
//    val questState = _networkState.asStateFlow()


    val data : MutableState<DataOrException<Questions, Boolean, Exception>> =
        mutableStateOf(DataOrException(null,true,Exception("")))

    var currentQuestionIndex by mutableIntStateOf(0)
    private set

    var selectedAnswer by mutableStateOf<String?>(null)
    private set

    var isAnswerSelected by mutableStateOf(false)
    private set

    var score by mutableIntStateOf(0)
    private set


    private var totalQuestions: Int = 0
    private var currentCategoryId : Int = 0
    private var currentDifficultyLevel: String = ""


    fun getAllQuestions(amount: Int = 15 , categoryId: Int, difficulty: String) {
        currentCategoryId = categoryId
        currentDifficultyLevel = difficulty

        viewModelScope.launch(Dispatchers.IO){
            data.value.loading = true

            try {
            data.value = repo.getAllQuestions(amount, categoryId, difficulty)
                val mappedQuestion = data.value.data?.results?.map { result ->
                    val allAnswers = mutableListOf<String>().apply {
                        add(result.correct_answer)
                        addAll(result.incorrect_answers)//.map { string -> Util.decodeHTMLText(string) })
                    }.shuffled()

                    Result(
                        category = result.category,
                        correct_answer = result.correct_answer,
                        incorrect_answers = result.incorrect_answers,
                        difficulty = result.difficulty,
                        question = result.question,
                        type = result.type,
                        answers = allAnswers
                    )
                }
                data.value.loading = false
                if (mappedQuestion != null) {
                    data.value.data?.results = mappedQuestion
                }


                totalQuestions = data.value.data?.results?.size ?: 0
                currentQuestionIndex = 0
                score = 0
                selectedAnswer = null
                isAnswerSelected = false
            }
            catch (e: Exception){
                data.value.loading = false
                data.value.e = e
            }

        }
    }

    fun onAnswerSelected(answer:String): String? {
        if(!isAnswerSelected){
            selectedAnswer = answer
            isAnswerSelected = true
            val currentQuestion = data.value.data?.results?.getOrNull(currentQuestionIndex)
            if(currentQuestion != null && answer == currentQuestion.correct_answer){
                score++
            }
        }
        return selectedAnswer
    }

    fun onNextQuestion(){
        if(currentQuestionIndex < (totalQuestions - 1)){
            currentQuestionIndex++
            selectedAnswer = null
            isAnswerSelected = false
        }
    }

    fun restartQuiz(){
        getAllQuestions(categoryId = currentCategoryId, difficulty = currentDifficultyLevel)
    }

    fun getCurrentQuestion() : Result? {
        return data.value.data?.results?.getOrNull(currentQuestionIndex)
    }


    fun getTotalQuestionsCount(): Int = totalQuestions



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