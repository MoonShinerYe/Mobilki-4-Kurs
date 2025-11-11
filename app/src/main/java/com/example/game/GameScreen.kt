package com.example.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.math.sqrt

data class Spider(
    val id: Int,
    var position: Offset,
    var speed: Offset,
    val radius: Float = 40f,
    var isAlive: Boolean = true
)

@Composable
fun GameScreen(
    onBack: () -> Unit = {}
) {
    var score by remember { mutableStateOf(0) }
    var gameTime by remember { mutableStateOf(0) }
    var spiders by remember { mutableStateOf<List<Spider>>(emptyList()) }
    var gameActive by remember { mutableStateOf(true) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var debugInfo by remember { mutableStateOf("Кликайте по паукам!") }
    var lastTap by remember { mutableStateOf<Offset?>(null) }
    var showGameOver by remember { mutableStateOf(false) }

    // Используем настройки из общего объекта
    val gameSpeed = GameSettings.gameSpeed
    val maxInsects = GameSettings.maxInsects
    val gameDuration = GameSettings.roundDuration // В секундах

    // Игровой цикл
    LaunchedEffect(gameActive) {
        while (gameActive) {
            delay(16) // ~60 FPS
            gameTime++

            // Проверяем, не истекло ли время игры
            val currentTimeInSeconds = gameTime / 60
            if (currentTimeInSeconds >= gameDuration) {
                gameActive = false
                showGameOver = true
                break
            }

            // Обновляем позиции пауков
            spiders = spiders.map { spider ->
                if (spider.isAlive) {
                    var newPosition = Offset(
                        spider.position.x + spider.speed.x * gameSpeed,
                        spider.position.y + spider.speed.y * gameSpeed
                    )

                    // Отскок от границ (используем реальный размер canvas)
                    if (newPosition.x < spider.radius || newPosition.x > canvasSize.width - spider.radius) {
                        spider.speed = spider.speed.copy(x = -spider.speed.x)
                    }
                    if (newPosition.y < spider.radius || newPosition.y > canvasSize.height - spider.radius) {
                        spider.speed = spider.speed.copy(y = -spider.speed.y)
                    }

                    newPosition = Offset(
                        spider.position.x + spider.speed.x * gameSpeed,
                        spider.position.y + spider.speed.y * gameSpeed
                    )
                    spider.copy(position = newPosition)
                } else {
                    spider
                }
            }

            // Удаляем мертвых пауков
            spiders = spiders.filter { it.isAlive }

            // Добавляем новых пауков каждые 2 секунды
            if (gameTime % 120 == 0 && spiders.size < maxInsects) {
                spiders = spiders + createNewSpider(spiders.size, canvasSize)
            }
        }
    }

    // Функция для обработки кликов (только если игра активна)
    fun handleTap(tapOffset: Offset) {
        if (!gameActive) return

        println("=== НОВЫЙ КЛИК ===")
        println("Координаты: $tapOffset")
        println("Всего пауков: ${spiders.size}")
        println("Живых пауков: ${spiders.count { it.isAlive }}")

        lastTap = tapOffset
        var hitCount = 0

        // Проверяем каждого паука на попадание
        spiders.forEach { spider ->
            if (spider.isAlive) {
                val distance = sqrt(
                    (tapOffset.x - spider.position.x) * (tapOffset.x - spider.position.x) +
                            (tapOffset.y - spider.position.y) * (tapOffset.y - spider.position.y)
                )

                val hitRadius = spider.radius * 1.2f

                println("Паук ${spider.id}: позиция=${spider.position}, радиус=${spider.radius}")
                println("Расстояние: $distance, зона попадания: $hitRadius")

                if (distance <= hitRadius) {
                    println(">>> ПОПАДАНИЕ В ПАУКА ${spider.id}! <<<")
                    hitCount++
                }
            }
        }

        if (hitCount > 0) {
            // Обновляем состояние пауков
            spiders = spiders.map { spider ->
                if (spider.isAlive) {
                    val distance = sqrt(
                        (tapOffset.x - spider.position.x) * (tapOffset.x - spider.position.x) +
                                (tapOffset.y - spider.position.y) * (tapOffset.y - spider.position.y)
                    )

                    if (distance <= spider.radius * 1.2f) {
                        spider.copy(isAlive = false)
                    } else {
                        spider
                    }
                } else {
                    spider
                }
            }

            score += 10 * hitCount
        } else {
        }
    }

    // Диалог окончания игры
    if (showGameOver) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Игра окончена!") },
            text = {
                Column {
                    Text("Ваш результат: $score очков")
                    Text("Убито пауков: ${spiders.count { !it.isAlive }}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Спасибо за игру!", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showGameOver = false
                        onBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50))
    ) {
        // Верхняя панель с информацией
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Очки: $score",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Время: ${gameTime / 60}",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Пауков: ${spiders.count { it.isAlive }}/$maxInsects",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Отладочная информация
        Text(
            text = if (gameActive) "Кликайте по паукам!" else "Игра окончена!",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Игровое поле с обработкой кликов
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(gameActive) {
                    detectTapGestures { tapOffset ->
                        handleTap(tapOffset)
                    }
                }
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                canvasSize = size

                // Рисуем место последнего клика
                lastTap?.let { tap ->
                    drawCircle(
                        color = Color.Yellow,
                        center = tap,
                        radius = 25f
                    )
                    drawCircle(
                        color = Color.Red,
                        center = tap,
                        radius = 5f
                    )
                }

                // Рисуем пауков
                spiders.forEach { spider ->
                    if (spider.isAlive) {
                        drawBlackWidowSpider(spider)
                    }
                }

                // Если игра окончена, показываем полупрозрачный overlay
                if (!gameActive) {
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f),
                        size = size
                    )
                }
            }

            // Сообщение об окончании игры поверх canvas
            if (!gameActive && !showGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Игра окончена!",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Очки: $score",
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Button(
                            onClick = onBack,
                            modifier = Modifier.padding(top = 24.dp)
                        ) {
                            Text("Вернуться в меню")
                        }
                    }
                }
            }
        }

        // Кнопка назад
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Выйти из игры")
        }
    }
}

// Функция для рисования паука черной вдовы
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBlackWidowSpider(spider: Spider) {
    val bodyRadius = spider.radius
    val headRadius = bodyRadius * 0.4f

    // Основное тело (черное, круглое)
    drawCircle(
        color = Color.Black,
        center = spider.position,
        radius = bodyRadius
    )

    // Голова (меньший черный круг)
    drawCircle(
        color = Color.Black,
        center = spider.position + Offset(-bodyRadius * 0.8f, 0f),
        radius = headRadius
    )

    // Характерное красное пятно на брюшке (форма песочных часов)
    drawCircle(
        color = Color.Red,
        center = spider.position + Offset(bodyRadius * 0.3f, 0f),
        radius = bodyRadius * 0.4f
    )

    // Верхняя часть красного пятна (форма песочных часов)
    drawCircle(
        color = Color.Red,
        center = spider.position + Offset(0f, -bodyRadius * 0.3f),
        radius = bodyRadius * 0.3f
    )

    // Нижняя часть красного пятна (форма песочных часов)
    drawCircle(
        color = Color.Red,
        center = spider.position + Offset(0f, bodyRadius * 0.3f),
        radius = bodyRadius * 0.3f
    )

    // Черный круг посередине чтобы создать форму песочных часов
    drawCircle(
        color = Color.Black,
        center = spider.position,
        radius = bodyRadius * 0.2f
    )

    // Глаза (красные, блестящие)
    val eyeColor = Color(0xFFFF5252)
    // Основные глаза
    drawCircle(
        color = eyeColor,
        center = spider.position + Offset(-bodyRadius * 0.9f, -headRadius * 0.2f),
        radius = headRadius * 0.3f
    )
    drawCircle(
        color = eyeColor,
        center = spider.position + Offset(-bodyRadius * 0.9f, headRadius * 0.2f),
        radius = headRadius * 0.3f
    )

    // Блики в глазах
    drawCircle(
        color = Color.White,
        center = spider.position + Offset(-bodyRadius * 0.95f, -headRadius * 0.25f),
        radius = headRadius * 0.1f
    )
    drawCircle(
        color = Color.White,
        center = spider.position + Offset(-bodyRadius * 0.95f, headRadius * 0.15f),
        radius = headRadius * 0.1f
    )

    // Лапки (8 ног, длинные и тонкие)
    val legColor = Color.Black
    val legLength = bodyRadius * 1.8f
    val legWidth = 3f

    // Передние лапки (2 пары)
    drawLine(
        color = legColor,
        start = spider.position + Offset(-bodyRadius * 0.3f, 0f),
        end = spider.position + Offset(-bodyRadius * 0.3f - legLength * 0.8f, -legLength * 0.6f),
        strokeWidth = legWidth
    )
    drawLine(
        color = legColor,
        start = spider.position + Offset(-bodyRadius * 0.3f, 0f),
        end = spider.position + Offset(-bodyRadius * 0.3f - legLength * 0.8f, legLength * 0.6f),
        strokeWidth = legWidth
    )

    drawLine(
        color = legColor,
        start = spider.position + Offset(-bodyRadius * 0.1f, 0f),
        end = spider.position + Offset(-bodyRadius * 0.1f - legLength * 0.9f, -legLength * 0.7f),
        strokeWidth = legWidth
    )
    drawLine(
        color = legColor,
        start = spider.position + Offset(-bodyRadius * 0.1f, 0f),
        end = spider.position + Offset(-bodyRadius * 0.1f - legLength * 0.9f, legLength * 0.7f),
        strokeWidth = legWidth
    )

    // Средние лапки (2 пары)
    drawLine(
        color = legColor,
        start = spider.position + Offset(bodyRadius * 0.1f, 0f),
        end = spider.position + Offset(bodyRadius * 0.1f + legLength * 0.7f, -legLength * 0.8f),
        strokeWidth = legWidth
    )
    drawLine(
        color = legColor,
        start = spider.position + Offset(bodyRadius * 0.1f, 0f),
        end = spider.position + Offset(bodyRadius * 0.1f + legLength * 0.7f, legLength * 0.8f),
        strokeWidth = legWidth
    )

    drawLine(
        color = legColor,
        start = spider.position + Offset(bodyRadius * 0.3f, 0f),
        end = spider.position + Offset(bodyRadius * 0.3f + legLength * 0.6f, -legLength * 0.9f),
        strokeWidth = legWidth
    )
    drawLine(
        color = legColor,
        start = spider.position + Offset(bodyRadius * 0.3f, 0f),
        end = spider.position + Offset(bodyRadius * 0.3f + legLength * 0.6f, legLength * 0.9f),
        strokeWidth = legWidth
    )

    // Отладочная зона клика (красный круг)
    val hitRadius = spider.radius * 1.2f
    drawCircle(
        color = Color.Red.copy(alpha = 0.3f),
        center = spider.position,
        radius = hitRadius,
        style = Stroke(width = 2f)
    )

    // ID паука
    drawContext.canvas.nativeCanvas.drawText(
        spider.id.toString(),
        spider.position.x,
        spider.position.y - bodyRadius - 15,
        android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 20f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    )
}

fun createNewSpider(id: Int, canvasSize: Size): Spider {
    val random = Random.Default

    // ОДИНАКОВАЯ СКОРОСТЬ для всех пауков
    val baseSpeed = 3.0f
    val angle = random.nextFloat() * 2 * Math.PI.toFloat()
    val speed = Offset(
        x = kotlin.math.cos(angle) * baseSpeed,
        y = kotlin.math.sin(angle) * baseSpeed
    )

    // Создаем паука в пределах canvas
    val position = if (canvasSize != Size.Zero) {
        Offset(
            x = random.nextFloat() * (canvasSize.width - 200) + 100,
            y = random.nextFloat() * (canvasSize.height - 200) + 100
        )
    } else {
        Offset(
            x = random.nextFloat() * 800 + 100,
            y = random.nextFloat() * 1200 + 100
        )
    }

    return Spider(
        id = id,
        position = position,
        speed = speed,
        radius = 30f + random.nextFloat() * 15f
    )
}