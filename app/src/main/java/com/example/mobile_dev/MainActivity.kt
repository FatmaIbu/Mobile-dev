package com.example.mobile_dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mobile_dev.data.AppDatabase
import com.example.mobile_dev.data.PreferencesManager
import com.example.mobile_dev.data.TaskRepository
import com.example.mobile_dev.ui.TaskViewModel
import com.example.mobile_dev.ui.TaskViewModelFactory
import com.example.mobile_dev.ui.navigation.TaskNavigation
import com.example.mobile_dev.ui.theme.TaskManagementTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(applicationContext)
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())

        // Get initial theme value
        val initialDarkTheme = runBlocking {
            preferencesManager.isDarkTheme.first()
        }

        setContent {
            var darkTheme by remember { mutableStateOf(initialDarkTheme) }

            // Collect theme changes
            LaunchedEffect(Unit) {
                preferencesManager.isDarkTheme.collect { isDark ->
                    darkTheme = isDark
                }
            }

            TaskManagementTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(repository)
                )

                TaskNavigation(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}