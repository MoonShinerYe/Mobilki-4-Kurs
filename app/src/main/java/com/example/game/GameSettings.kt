package com.example.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object GameSettings {
    var gameSpeed by mutableStateOf(20f)
    var maxInsects by mutableStateOf(10)
    var bonusInterval by mutableStateOf(15)
    var roundDuration by mutableStateOf(60)
}