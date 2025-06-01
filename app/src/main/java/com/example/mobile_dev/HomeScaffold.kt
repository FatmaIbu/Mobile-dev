package com.example.mobile_dev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScaffold(currentRoute: String, onNavItemSelected: (String) -> Unit ,onAddTaskClick: () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomBar(currentRoute = currentRoute) { route ->
                onNavItemSelected(route)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        HomeScreen(
            userName = "John",
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}