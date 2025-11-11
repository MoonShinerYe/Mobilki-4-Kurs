package com.example.game

import java.util.Calendar

data class Player(
    val fullName: String,
    val gender: String,
    val course: String,
    val difficultyLevel: Int,
    val birthDate: Calendar,
    val zodiacSign: String
) {
    companion object {
        fun getZodiacSign(day: Int, month: Int): String {
            return when (month) {
                0 -> if (day < 20) "Козерог" else "Водолей"  // Январь
                1 -> if (day < 19) "Водолей" else "Рыбы"     // Февраль
                2 -> if (day < 21) "Рыбы" else "Овен"        // Март
                3 -> if (day < 20) "Овен" else "Телец"       // Апрель
                4 -> if (day < 21) "Телец" else "Близнецы"   // Май
                5 -> if (day < 21) "Близнецы" else "Рак"     // Июнь
                6 -> if (day < 23) "Рак" else "Лев"          // Июль
                7 -> if (day < 23) "Лев" else "Дева"         // Август
                8 -> if (day < 23) "Дева" else "Весы"        // Сентябрь
                9 -> if (day < 23) "Весы" else "Скорпион"    // Октябрь
                10 -> if (day < 22) "Скорпион" else "Стрелец" // Ноябрь
                11 -> if (day < 22) "Стрелец" else "Козерог" // Декабрь
                else -> "Неизвестно"
            }
        }
    }
}