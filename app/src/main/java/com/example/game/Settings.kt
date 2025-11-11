package com.example.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Settings() {
    // Используем настройки из общего объекта
    var gameSpeed by remember { mutableStateOf(GameSettings.gameSpeed) }
    var maxInsects by remember { mutableStateOf(GameSettings.maxInsects) }
    var bonusInterval by remember { mutableStateOf(GameSettings.bonusInterval) }
    var roundDuration by remember { mutableStateOf(GameSettings.roundDuration) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Настройки игры",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Скорость игры
        Text("Скорость игры: ${gameSpeed.toInt()}", fontWeight = FontWeight.Medium)
        Slider(
            value = gameSpeed,
            onValueChange = { gameSpeed = it },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Максимальное количество насекомых
        Text("Макс. насекомых: $maxInsects", fontWeight = FontWeight.Medium)
        Slider(
            value = maxInsects.toFloat(),
            onValueChange = { maxInsects = it.toInt() },
            valueRange = 5f..20f,
            steps = 14,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Интервал бонусов
        Text("Интервал бонусов: ${bonusInterval}сек", fontWeight = FontWeight.Medium)
        Slider(
            value = bonusInterval.toFloat(),
            onValueChange = { bonusInterval = it.toInt() },
            valueRange = 5f..30f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Длительность раунда
        Text("Длительность раунда: ${roundDuration}сек", fontWeight = FontWeight.Medium)
        Slider(
            value = roundDuration.toFloat(),
            onValueChange = { roundDuration = it.toInt() },
            valueRange = 30f..120f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка сохранения
        Button(
            onClick = {
                // Сохраняем настройки в общий объект
                GameSettings.gameSpeed = gameSpeed
                GameSettings.maxInsects = maxInsects
                GameSettings.bonusInterval = bonusInterval
                GameSettings.roundDuration = roundDuration
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить настройки")
        }
    }
}