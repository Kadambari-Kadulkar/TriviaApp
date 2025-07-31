package com.example.triviaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.triviaapp.util.AppColors
import com.example.triviaapp.util.Util


@Composable
fun Home(navHostController: NavHostController) {
    val categories = mapOf(
        "General Knowledge" to 15,
        "Science:Computers" to 18,
        "Entertainment: Films" to 11,
        "Entertainment: Books" to 10,
        "Sports" to 21,
        "History" to 23,
        "Politics" to 24,
        "Art" to 25
    )
    val difficulties = listOf("easy", "medium", "hard")
    var selectedCategory by remember { mutableStateOf(categories.keys.first()) }
    var selectedDifficulties by remember { mutableStateOf(difficulties.first()) }

    var expandedCategory by remember { mutableStateOf(false) }
    var expandedDifficulty by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(Util.LBL_SELECT_CATEGORY, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))

        //Category Box Component
        Box {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(Util.CATEGORY_LABLE) },
                trailingIcon = {
                    IconButton(onClick = { expandedCategory = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Category dropdown")
                    }
                }
            )
            DropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }, modifier = Modifier.width(300.dp)) {
                categories.keys.forEach { category ->
                    DropdownMenuItem(text = { Text(category) }, onClick = {
                        selectedCategory = category
                        expandedCategory = false
                    })

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = Util.LBL_SELECT_DIFFICULTY, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))

        //Difficulty Level Box Component
        Box {
            OutlinedTextField(
                value = selectedDifficulties.replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(Util.DIFFICULTY_LABLE) },
                trailingIcon = {
                    IconButton(onClick = { expandedDifficulty = true }) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Difficulty dropdown"
                        )
                    }
                })
            DropdownMenu(expandedDifficulty, onDismissRequest = { expandedDifficulty = false }, Modifier.width(300.dp)) {
                difficulties.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            selectedDifficulties = level
                            expandedDifficulty = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val categoryId = categories[selectedCategory] ?: 15
            navHostController.navigate("question_screen/${categoryId}/${selectedDifficulties}")
        }) {
            Text(Util.BTN_START_QUIZ)
        }
    }
}