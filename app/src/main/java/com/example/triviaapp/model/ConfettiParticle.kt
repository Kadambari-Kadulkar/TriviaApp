package com.example.triviaapp.model

import androidx.compose.ui.graphics.Color

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val velocityY : Float,
    val color: Color,
    val size : Float
)
