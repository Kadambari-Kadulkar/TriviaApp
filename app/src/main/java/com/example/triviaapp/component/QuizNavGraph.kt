package com.example.triviaapp.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun QuizNavGraph(
    //navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home_screen",
        modifier = modifier
    ) {

        //Home Screen
        composable("home_screen") {
            Home(navHostController = navController)
        }
        //Quiz Screen
        composable(
            route = "question_screen/{categoryId}/{difficulty}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val categoryId = navBackStackEntry.arguments?.getInt("categoryId") ?: 15
            val difficulty = navBackStackEntry.arguments?.getString("difficulty") ?: "easy"

            Questions(navController, categoryId, difficulty)
        }
        //Result Screen
        composable(
            route = "quiz_completion/{categoryId}/{difficulty}/{score}/{total}", arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val score = navBackStackEntry.arguments?.getInt("score") ?: 0
            val total = navBackStackEntry.arguments?.getInt("total") ?: 15
            val categoryId = navBackStackEntry.arguments?.getInt("categoryId") ?: 15
            val difficulty = navBackStackEntry.arguments?.getString("difficulty") ?: "easy"


            QuizCompletion(
                navController,
                categoryId = categoryId,
                difficulty = difficulty,
                score = score,
                total = total
            )
        }

    }
}