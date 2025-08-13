package com.example.triviaapp.component

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.triviaapp.model.ConfettiParticle
import com.example.triviaapp.view.QuestionsViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@SuppressLint("ContextCastToActivity")
@Composable
fun QuizCompletion(
    navController: NavHostController,
    viewModel: QuestionsViewModel = hiltViewModel(),
    categoryId: Int, difficulty: String,
    score: Int, total: Int
) {

    val activity = LocalContext.current as Activity
    ConfettiAnimation()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val resultText = when(score){
                in 0..5 -> "Good attempt, Give another try."
                in 6..10 -> "Not Bad, You did Well."
                else -> { "Well Done, That's amazing!!" }
            }

            Text("Quiz Completed", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(12.dp))

            Text(resultText, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Your Score: $score / $total", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(32.dp))

            //Restart Quiz
            Button(
                onClick = {
                    viewModel.restartQuiz()
                    navController.navigate("question_screen/$categoryId/$difficulty") {
                        popUpTo("result_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restart Quiz")
            }


            Spacer(modifier = Modifier.height(12.dp))

            //Go to Home Screen
            Button(
                onClick = {
                    navController.navigate("home_screen") {
                        popUpTo("home_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
            {
                Text("Home")
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Quit Quiz
            Button(
                onClick = {
                    activity.finish()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            )
            {
                Text("Quit")
            }
        }
    }
}


@Composable
fun ConfettiAnimation(modifier: Modifier = Modifier, onAnimationEnd: () -> Unit = {}) {
    var particles by remember { mutableStateOf(emptyList<ConfettiParticle>()) }

    LaunchedEffect(Unit)
    {
        particles = List(100) {
            ConfettiParticle(
                x = Random.nextFloat() * 1200f,
                y = Random.nextFloat() * -1200f,
                velocityY = Random.nextFloat() * 28f + 28f,
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                    alpha = 1f
                ),
                size = Random.nextFloat() * 12f + 4f
            )
        }
        repeat(100) {
            delay(16)
            particles = particles.map { it.copy(y = it.y + it.velocityY) }
        }
        //onAnimationEnd()
    }

    Canvas(modifier = modifier.fillMaxSize())
    {
        particles.forEach {
            drawCircle(
                color = it.color,
                radius = it.size,
                center = Offset(it.x, it.y)
            )
        }
    }

}