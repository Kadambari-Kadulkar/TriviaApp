package com.example.triviaapp.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.triviaapp.data.NetworkResultState
import com.example.triviaapp.ui.theme.TriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaAppTheme {
              LoadQuestions()

                }
            }
        }
    }



@Composable
fun LoadQuestions(viewModel: QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)

}

@Composable
fun Questions(viewModel: QuestionsViewModel){
        val questions = viewModel.data.value.data?.results?.toMutableList() ?: emptyList()
        Log.e("questions: ", if(questions.isNotEmpty()) questions.size.toString() else "No Questions")
}

