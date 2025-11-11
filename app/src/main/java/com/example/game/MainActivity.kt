package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game.ui.theme.GameTheme

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
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentDestination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Регистрация") },
                    label = { Text("Регистрация") },
                    selected = currentRoute == "registration",
                    onClick = { navController.navigate("registration") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Правила") },
                    label = { Text("Правила") },
                    selected = currentRoute == "rules",
                    onClick = { navController.navigate("rules") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Авторы") },
                    label = { Text("Авторы") },
                    selected = currentRoute == "authors",
                    onClick = { navController.navigate("authors") }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Настройки") },
                    label = { Text("Настройки") },
                    selected = currentRoute == "settings",
                    onClick = { navController.navigate("settings") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "registration",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("registration") {
                RegistrationScreen()
            }

            composable("rules") {
                Rules()
            }
            composable("authors") {
                Authors()
            }
            composable("settings") {
                Settings()
            }
        }
    }
}