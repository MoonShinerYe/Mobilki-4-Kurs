package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.game.ui.theme.GameTheme  // ← ДОБАВЬТЕ ЭТОТ ИМПОРТ

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("main") }
                    var registeredPlayer by remember { mutableStateOf<Player?>(null) }

                    when (currentScreen) {
                        "main" -> MainScreen(
                            onNavigateToRegistration = { currentScreen = "registration" },
                            player = registeredPlayer
                        )
                        "registration" -> RegistrationScreen(
                            onRegistrationComplete = { player ->
                                registeredPlayer = player
                                currentScreen = "main"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToRegistration: () -> Unit,
    player: Player?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Игра Жуки",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToRegistration,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Регистрация игрока")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Показываем информацию о зарегистрированном игроке
        player?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Зарегистрированный игрок:",
                        fontWeight = FontWeight.Bold
                    )
                    Text("ФИО: ${it.fullName}")
                    Text("Курс: ${it.course}")
                    Text("Уровень: ${it.difficultyLevel}")
                    Text("Знак зодиака: ${it.zodiacSign}")
                }
            }
        }
    }
}