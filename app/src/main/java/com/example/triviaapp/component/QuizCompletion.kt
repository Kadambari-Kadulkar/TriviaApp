package com.example.triviaapp.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun QuizCompletion(navController: NavHostController, score: Int, total: Int) {
    Text("Quiz Completion screen, Score: $score out of $total", fontSize = 14.sp)
}