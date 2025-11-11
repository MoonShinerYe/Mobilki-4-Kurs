package com.example.game

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
fun RegistrationScreen() {
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf(1) }
    var difficultyLevel by remember { mutableStateOf(3) }

    // Простая дата вместо Calendar
    var day by remember { mutableStateOf("1") }
    var month by remember { mutableStateOf("1") }
    var year by remember { mutableStateOf("2000") }

    val zodiacSign = remember(day, month, year) {
        getZodiacSign(day.toIntOrNull() ?: 1, (month.toIntOrNull() ?: 1) - 1)
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
        val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс")

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

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
                label = { Text("День") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = month,
                onValueChange = { month = it },
                label = { Text("Месяц") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год") },
                modifier = Modifier.weight(1f)
            )
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
                    Text("ФИО: $fullName")
                    Text("Пол: $gender")
                    Text("Курс: ${courses[selectedCourse - 1]}")
                    Text("Уровень сложности: $difficultyLevel")
                    Text("Дата рождения: $day.$month.$year")
                    Text("Знак зодиака: $zodiacSign")
                }
            }
        }
    }
}

// Функция расчета знака зодиака
fun getZodiacSign(day: Int, month: Int): String {
    return when (month) {
        0 -> if (day < 20) "Козерог" else "Водолей"
        1 -> if (day < 19) "Водолей" else "Рыбы"
        2 -> if (day < 21) "Рыбы" else "Овен"
        3 -> if (day < 20) "Овен" else "Телец"
        4 -> if (day < 21) "Телец" else "Близнецы"
        5 -> if (day < 21) "Близнецы" else "Рак"
        6 -> if (day < 23) "Рак" else "Лев"
        7 -> if (day < 23) "Лев" else "Дева"
        8 -> if (day < 23) "Дева" else "Весы"
        9 -> if (day < 23) "Весы" else "Скорпион"
        10 -> if (day < 22) "Скорпион" else "Стрелец"
        11 -> if (day < 22) "Стрелец" else "Козерог"
        else -> "Неизвестно"
    }
}