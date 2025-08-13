package com.example.triviaapp.component


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.triviaapp.model.ConfettiParticle
import com.example.triviaapp.util.AppColors
import com.example.triviaapp.util.Util
import com.example.triviaapp.view.QuestionsViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun Questions(navController:NavController,
              categoryId: Int,
              difficulty: String,
              viewModel: QuestionsViewModel= hiltViewModel()) {


    // Direct state access via delegation
    val questionState = viewModel.data.value
    val currentQuestionIndex = viewModel.currentQuestionIndex
    val selectedAnswer = viewModel.selectedAnswer
    val isAnswerSelected = viewModel.isAnswerSelected
    val score = viewModel.score

    //Load questions on screen launch
    LaunchedEffect(Unit) {
        viewModel.getAllQuestions(15, categoryId, difficulty)
    }

    if (questionState.loading == true) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val question = viewModel.getCurrentQuestion()
        question?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            "Question ${currentQuestionIndex + 1} of ${viewModel.getTotalQuestionsCount()}",
                            fontSize = 20.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Score",
                            style = MaterialTheme.typography.labelMedium,
                            color = AppColors.mBlack
                        )

                        AnimatedContent(
                            targetState = score,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                                    animationSpec = tween(300)
                                )
                            },
                            label = "ScoreAnimation"
                        ) { animatedScore ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Transparent, shape = CircleShape)
                                    .border(1.dp, color = AppColors.mBlack, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$animatedScore",
                                    color = AppColors.mBlack,
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(Util.decodeHTMLText(question.question),
                    fontSize = 20.sp,
                    color = AppColors.mBlack,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp)

                Spacer(modifier = Modifier.height(20.dp))
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
                DrawSeparator(pathEffect)
                Spacer(modifier = Modifier.height(40.dp))

                Column {
                    question.answers.forEach { answer ->
                        val isCorrect = answer == question.correct_answer
                        val isSelected = selectedAnswer == answer

                        val textColor = when  {
                            isAnswerSelected && isSelected && isCorrect -> AppColors.mGreen
                            isAnswerSelected && isSelected && !isCorrect -> Color.Red
                            isAnswerSelected && !isSelected && isCorrect -> AppColors.mGreen
                            else -> AppColors.mBlack

                    }
                        val radioColor = when {
                            isAnswerSelected && isSelected && isCorrect -> AppColors.mGreen
                            isAnswerSelected && isSelected && !isCorrect -> Color.Red
                            isAnswerSelected && !isSelected && isCorrect -> AppColors.mGreen
                            else -> AppColors.mBlack
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(55.dp)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            AppColors.mLightGray,
                                            AppColors.mLightGray
                                        )
                                    ),
                                    shape = RoundedCornerShape(
                                        topStartPercent = 30,
                                        topEndPercent = 30,
                                        bottomEndPercent = 30,
                                        bottomStartPercent = 30
                                    )
                                )
                                .clickable(
                                    enabled = !isAnswerSelected,
                                    onClick = { viewModel.onAnswerSelected(answer) }
                                )
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    if (!isAnswerSelected) {
                                        viewModel.onAnswerSelected(answer)
                                    }
                                },
                                enabled = !isAnswerSelected,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = radioColor,
                                    unselectedColor = AppColors.mBlack,
                                    disabledSelectedColor = radioColor
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = Util.decodeHTMLText(answer), color = textColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                //Next Button
                Button(
                    onClick = {
                        if (currentQuestionIndex + 1 == viewModel.getTotalQuestionsCount()) {
                            val totalQuestions = viewModel.getTotalQuestionsCount()
                            navController.navigate("quiz_completion/$categoryId/$difficulty/$score/$totalQuestions") {
                                popUpTo("home_screen") {
                                    inclusive = false
                                }
                            }
                        } else {
                            viewModel.onNextQuestion()
                        }
                    }, enabled = isAnswerSelected,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun DrawSeparator(pathEffect : PathEffect){
    Canvas(modifier = Modifier
        .paddingFromBaseline(18.dp, 0.dp)
        .fillMaxWidth()
        .height(1.dp)) {
        drawLine(color = AppColors.mBlack, start = Offset(0f,0f), end = Offset(size.width,0f), pathEffect = pathEffect)
    }
}
