package com.example.triviaapp.component

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.htmlEncode
import com.example.triviaapp.model.Questions
import com.example.triviaapp.model.Result
import com.example.triviaapp.util.AppColors
import com.example.triviaapp.view.QuestionsViewModel

@Composable
fun Questions(viewModel: QuestionsViewModel){
    val questions = viewModel.data.value.data?.results?.toMutableList() ?: emptyList()
    //Log.e("questions: ", if(questions.isNotEmpty()) questions.toString() else "No Questions")
    val questionIndex = remember {
        mutableStateOf(0)
    }
    if(viewModel.data.value.loading == true){
        CircularProgressIndicator()
    }else{
        val question =
            try {
                questions[questionIndex.value]
            } catch (ex: Exception) {
                null
                //questions.first()
            }
        if (questions.isNotEmpty()) {
            if (question != null) {
                QuestionsDisplay(questions = question, questionIndex, viewModel) {
                    questionIndex.value += 1
                }
            }
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
    Log.e("questionIndex",questionIndex.value.toString())
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
            QuestionTracker(counter = questionIndex.value, 10)
            DrawSeparator(pathEffect)

            Column { Text(text= questions.question,
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
                                    Color.Green.copy(alpha = 0.5f)
                            }else{
                                Color.Red.copy(alpha = 0.5f)
                            }))
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                            color = if(correctAnswerState.value == true && answerState.value == answer){
                            Color.Green//.copy(alpha = 0.5f)
                        }else if(correctAnswerState.value == false && answerState.value == answer){
                            Color.Red//.copy(alpha = 0.5f)
                        }else{
                            AppColors.mBlack
                        }, fontSize = 17.sp)){
                            append(answer)
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
                    colors= ButtonDefaults.buttonColors(AppColors.mOffWhite)) {
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
        modifier =  Modifier.padding(20.dp))
    
}

@Composable
fun DrawSeparator(pathEffect : PathEffect){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        drawLine(color = AppColors.mBlack, start = Offset(0f,0f), end = Offset(size.width,0f), pathEffect = pathEffect)
    }
}