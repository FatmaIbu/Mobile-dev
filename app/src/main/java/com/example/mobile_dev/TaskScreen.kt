package com.example.mobile_dev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Task(val title: String, val description: String, val isDone: Boolean)

@Composable
fun TasksScreen(tasks: List<Task> = sampleTasks) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
            .padding(16.dp)
    ) {
        Text("My Tasks", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tasks) { task ->
                TaskItem(task = task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (task.isDone) Color(0xFFDFF5DC) else Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(task.title, fontWeight = FontWeight.Bold)
                Text(task.description, fontSize = 12.sp, color = Color.Gray)
            }
            Checkbox(checked = task.isDone, onCheckedChange = {})
        }
    }
}

val sampleTasks = listOf(
    Task("Design onboarding screen", "Sketch layout in Figma", false),
    Task("Implement login UI", "Build with Compose", true),
    Task("Fix navigation bug", "Login to Home flow", false)
)
@Preview(showBackground = true)
@Composable
fun PreviewTasksScreen() {
    TasksScreen()
}
