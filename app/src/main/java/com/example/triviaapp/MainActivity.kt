package com.example.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.triviaapp.component.QuizNavGraph
import com.example.triviaapp.ui.theme.TriviaAppTheme
import com.example.triviaapp.view.TriviaHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        //window.statusBarColor = Color.TRANSPARENT
        setContent {
            TriviaAppTheme {
                QuizNavGraph()
                //TriviaHome()
            }
            }
        }
    }







