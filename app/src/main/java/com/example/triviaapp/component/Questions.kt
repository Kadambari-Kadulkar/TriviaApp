package com.example.triviaapp.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.triviaapp.model.ConfettiParticle
import com.example.triviaapp.model.Result
import com.example.triviaapp.util.AppColors
import com.example.triviaapp.util.Util
import com.example.triviaapp.view.QuestionsViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun Questions(navController:NavController,categoryId: Int, difficulty: String, viewModel: QuestionsViewModel= hiltViewModel()){
    val questions = viewModel.data.value.data?.results?.toMutableList() ?: emptyList()
    //Log.e("questions: ", if(questions.isNotEmpty()) questions.toString() else "No Questions")
    val questionIndex = remember {
        mutableStateOf(1)
    }
    val showCompletion = remember { mutableStateOf(false) }
    if(viewModel.data.value.loading == true){
        CircularProgressIndicator()
    }else{
//        val question =
//            try {
//                questions[questionIndex.value]
//            } catch (ex: Exception) {
//                null
//                //questions.first()
//            }
        if (questions.isNotEmpty()) {
            if(showCompletion.value){
                QuizCompletionScreen(onRestart =  {
                    questionIndex.value = 0
                    showCompletion.value = false
                    //viewModel.refreshData()
                })
            }else{
                val question = questions.getOrNull(questionIndex.value)
                question.let {
                    if (question != null) {
                        QuestionsDisplay(questions = question, questionIndex,viewModel){
                            if(questionIndex.value < questions.size - 1){
                                questionIndex.value+=1
                            }else{
                                showCompletion.value = true
                            }
                        }
                    }
                }
            }
//            if (question != null) {
//                QuestionsDisplay(questions = question, questionIndex, viewModel) {
//                    questionIndex.value += 1
//                }
//            }
        }
    }
}


@Composable
fun QuestionsDisplay(
    questions: Result,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit = {}
){
    val answersList = questions.incorrect_answers.toMutableList()
    answersList.add(questions.correct_answer)
    val choiceState = remember (questions){
        answersList
        //questions.incorrect_answers.toMutableList().add(questions.correct_answer)
    }
    val answerState = remember (questions) {
        mutableStateOf<String?>(null)
    }

    val correctAnswerState = remember (questions){
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer : (String) -> Unit = remember (questions){
        {
            answerState.value = it
            correctAnswerState.value = it == questions.correct_answer
        }
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        color = AppColors.mOffWhite
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker(counter = questionIndex.value, viewModel.getTotalQuestionsCount())
            if(questionIndex.value >=3) ShowProgress(score = questionIndex.value, viewModel.getTotalQuestionsCount())
            DrawSeparator(pathEffect)

            Column {
                Text(text = Util.decodeHTMLText(questions.question),
                modifier = Modifier
                    .padding(16.dp, 16.dp, 6.dp, 6.dp)
                    .align(Alignment.Start)
                    .fillMaxHeight(0.3f),
                fontSize = 20.sp,
                color = AppColors.mBlack,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp) }

            //Choices
            choiceState.forEachIndexed { index, answer ->
                Row (Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
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
                    .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically){
                        RadioButton(selected = (answerState.value == answer),
                            onClick ={updateAnswer(answer)
                            }, modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                    if(correctAnswerState.value == true && answerState.value == answer){
                                        AppColors.mGreen
                            }else{
                                Color.Red.copy(alpha = 0.5f)
                            }))
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                            color = if(correctAnswerState.value == true && answerState.value == answer){
                                AppColors.mGreen
                        }else if(correctAnswerState.value == false && answerState.value == answer){
                            Color.Red//.copy(alpha = 0.5f)
                        }else{
                            AppColors.mBlack
                        }, fontSize = 17.sp)){
                            append(Util.decodeHTMLText(answer))
                        }
                    }
                    Text(annotatedString, Modifier.padding(6.dp))

                }
            }
            Button(onClick = { onNextClicked(questionIndex.value) } , modifier = Modifier
                .width(130.dp)
                .padding(3.dp)
                .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(2.dp,AppColors.mLightGray),
                    colors= buttonColors(AppColors.mOffWhite)) {
                    Text(text = "Next", modifier = Modifier.padding(4.dp),color = AppColors.mBlack, fontSize = 17.sp)
                }

        }
    }
}

@Composable
fun QuestionTracker(counter:Int = 10,
                    outOf:Int = 100){
    Text(text = buildAnnotatedString { withStyle(style= ParagraphStyle(textIndent = TextIndent.None)){
         withStyle(style= SpanStyle(color = AppColors.mDarkPurple, fontWeight = FontWeight.Bold, fontSize = 27.sp)){
             append("Question $counter/")
             withStyle(style = SpanStyle(color = AppColors.mDarkPurple, fontWeight = FontWeight.Light, fontSize = 14.sp)){
                 append("$outOf")
             }
         }
    } },
        modifier =  Modifier.padding(13.dp))
    
}

@Composable
fun DrawSeparator(pathEffect : PathEffect){
    Canvas(modifier = Modifier.paddingFromBaseline(18.dp,0.dp)
        .fillMaxWidth()
        .height(1.dp)) {
        drawLine(color = AppColors.mBlack, start = Offset(0f,0f), end = Offset(size.width,0f), pathEffect = pathEffect)
    }
}

@Composable
fun ShowProgress(score: Int = 3, totalQuestionsCount: Int){
    val gradient = Brush.linearGradient(listOf(AppColors.mBlack,AppColors.mBlack))
    val progressFactor by remember(score) {
        mutableStateOf(score / totalQuestionsCount.toFloat())
    }
    Row (modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(40.dp)
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColors.mLightGray,
                    AppColors.mLightGray
                )
            ),
            shape = RoundedCornerShape(30.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomStartPercent = 50,
                bottomEndPercent = 50
            )
        )
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically){
            Button(
                contentPadding = PaddingValues(1.dp),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth(progressFactor)
                    .background(brush = gradient),
                enabled = false,
                elevation = null,
                colors = buttonColors(Color.Transparent, Color.Transparent)

            ) {
                Text( text = (score * 10).toString(),
                    modifier = Modifier
                    //.clip(RoundedCornerShape(8.dp))
                    .fillMaxHeight(0.75f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),
                    color = AppColors.mOffWhite,
                    textAlign = TextAlign.Center)
            }
    }
}

@Composable
fun QuizCompletionScreen(onRestart:() -> Unit){
    Box (modifier = Modifier.fillMaxSize()){
        ConfettiAnimation(modifier = Modifier.fillMaxSize())

        Column (modifier = Modifier
            .align(Alignment.Center)
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "🎉 Quiz Completed! \uD83C\uDF89",
                style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRestart){
                Text("Restart Quiz")
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