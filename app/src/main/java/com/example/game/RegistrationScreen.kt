package com.example.game

import com.example.game.ui.theme.GameTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegistrationComplete: (Player) -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf(1) }
    var difficultyLevel by remember { mutableStateOf(3) }
    var birthDate by remember { mutableStateOf(Calendar.getInstance()) }

    val zodiacSign = remember(birthDate) {
        Player.getZodiacSign(
            birthDate.get(Calendar.DAY_OF_MONTH),
            birthDate.get(Calendar.MONTH)
        )
    }

    var showResult by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Регистрация игрока",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // ФИО
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        // Пол
        Text("Пол", fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "Мужской",
                    onClick = { gender = "Мужской" }
                )
                Text("Мужской", modifier = Modifier.padding(start = 4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "Женский",
                    onClick = { gender = "Женский" }
                )
                Text("Женский", modifier = Modifier.padding(start = 4.dp))
            }
        }

        // Курс
        Text("Курс", fontWeight = FontWeight.Medium)
        var expanded by remember { mutableStateOf(false) }
        val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс", "5 курс", "6 курс")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = courses[selectedCourse - 1],
                onValueChange = {},
                label = { Text("Выберите курс") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                courses.forEachIndexed { index, course ->
                    DropdownMenuItem(
                        text = { Text(course) },
                        onClick = {
                            selectedCourse = index + 1
                            expanded = false
                        }
                    )
                }
            }
        }

        // Уровень сложности
        Text("Уровень сложности: $difficultyLevel", fontWeight = FontWeight.Medium)
        Slider(
            value = difficultyLevel.toFloat(),
            onValueChange = { difficultyLevel = it.toInt() },
            valueRange = 1f..6f,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = when (difficultyLevel) {
                1 -> "Очень легкий"
                2 -> "Легкий"
                3 -> "Средний"
                4 -> "Сложный"
                5 -> "Очень сложный"
                6 -> "Эксперт"
                else -> "Неизвестно"
            },
            fontSize = 14.sp
        )

        // Дата рождения
        Text("Дата рождения", fontWeight = FontWeight.Medium)

        // Простой выбор даты
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // День
                var day by remember {
                    mutableStateOf(birthDate.get(Calendar.DAY_OF_MONTH).toString())
                }
                OutlinedTextField(
                    value = day,
                    onValueChange = {
                        day = it
                        birthDate.set(Calendar.DAY_OF_MONTH, it.toIntOrNull() ?: 1)
                    },
                    label = { Text("День") },
                    modifier = Modifier.weight(1f)
                )

                // Месяц
                var month by remember {
                    mutableStateOf((birthDate.get(Calendar.MONTH) + 1).toString())
                }
                OutlinedTextField(
                    value = month,
                    onValueChange = {
                        month = it
                        birthDate.set(Calendar.MONTH, (it.toIntOrNull() ?: 1) - 1)
                    },
                    label = { Text("Месяц") },
                    modifier = Modifier.weight(1f)
                )

                // Год
                var year by remember {
                    mutableStateOf(birthDate.get(Calendar.YEAR).toString())
                }
                OutlinedTextField(
                    value = year,
                    onValueChange = {
                        year = it
                        birthDate.set(Calendar.YEAR, it.toIntOrNull() ?: 2000)
                    },
                    label = { Text("Год") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Знак зодиака
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Знак зодиака", fontWeight = FontWeight.Bold)
                Text(
                    text = zodiacSign,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Кнопка регистрации
        Button(
            onClick = {
                if (fullName.isNotEmpty() && gender.isNotEmpty()) {
                    val player = Player(
                        fullName = fullName,
                        gender = gender,
                        course = courses[selectedCourse - 1],
                        difficultyLevel = difficultyLevel,
                        birthDate = birthDate,
                        zodiacSign = zodiacSign
                    )
                    onRegistrationComplete(player)
                    showResult = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = fullName.isNotEmpty() && gender.isNotEmpty()
        ) {
            Text("Зарегистрировать")
        }

        // Результат
        if (showResult) {
            RegistrationResult(
                player = Player(
                    fullName = fullName,
                    gender = gender,
                    course = courses[selectedCourse - 1],
                    difficultyLevel = difficultyLevel,
                    birthDate = birthDate,
                    zodiacSign = zodiacSign
                )
            )
        }
    }
}

@Composable
fun RegistrationResult(player: Player) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Регистрация завершена!",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text("ФИО: ${player.fullName}")
            Text("Пол: ${player.gender}")
            Text("Курс: ${player.course}")
            Text("Уровень сложности: ${player.difficultyLevel}")
            Text("Дата рождения: ${player.birthDate.get(Calendar.DAY_OF_MONTH)}.${player.birthDate.get(Calendar.MONTH) + 1}.${player.birthDate.get(Calendar.YEAR)}")
            Text("Знак зодиака: ${player.zodiacSign}")
        }
    }
}