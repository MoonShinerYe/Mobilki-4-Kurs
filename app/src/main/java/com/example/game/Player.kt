package com.example.game

import java.util.Calendar

data class Player(
    val fullName: String,
    val gender: String,
    val course: String,
    val difficultyLevel: Int,
    val birthDate: Calendar,
    val zodiacSign: String
)