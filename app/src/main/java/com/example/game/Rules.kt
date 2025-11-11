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
fun Rules() {
    val htmlText = """
        <h1>Правила игры "Жуки"</h1>
        <h2>Основная цель</h2>
        <p>Уничтожайте насекомых, появляющихся на экране, набирая как можно больше очков.</p>
        
        <h2>Управление</h2>
        <ul>
            <li>Нажимайте на насекомых для их уничтожения</li>
            <li>За каждое насекомое начисляются очки</li>
            <li>За промах - штрафные очки</li>
        </ul>
        
        <h2>Уровни сложности</h2>
        <ol>
            <li><b>Очень легкий</b> - медленные насекомые</li>
            <li><b>Легкий</b> - средняя скорость</li>
            <li><b>Средний</b> - быстрые насекомые</li>
            <li><b>Сложный</b> - очень быстрые</li>
            <li><b>Эксперт</b> - максимальная скорость</li>
        </ol>
        
        <h2>Бонусы</h2>
        <p>Каждые 15 секунд появляются бонусы, дающие специальные возможности.</p>
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Правила игры",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = htmlText.replace(Regex("<[^>]*>"), ""),
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
    }
}