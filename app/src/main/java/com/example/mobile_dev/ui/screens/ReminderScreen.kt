package com.example.mobile_dev.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.Task
import com.example.mobile_dev.ui.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    viewModel: TaskViewModel,
    onBackClick: () -> Unit,
    onTaskClick: (Long) -> Unit
) {
    val upcomingReminders by viewModel.upcomingReminders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminders") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (upcomingReminders.isEmpty()) {
            EmptyState(
                title = "No Reminders",
                message = "You don't have any upcoming reminders",
                image = R.drawable.empty_reminders
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(upcomingReminders.sortedBy { it.reminderTime }) { task ->
                    ReminderItem(
                        task = task,
                        onClick = { onTaskClick(task.id) },
                        onDismiss = { viewModel.updateTaskReminder(task.id, null) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderItem(
    task: Task,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss Reminder")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                task.reminderTime?.let { reminderTime ->
                    val timeUntil = ChronoUnit.MINUTES.between(LocalDateTime.now(), reminderTime)
                    val formattedTime = when {
                        timeUntil < 60 -> "$timeUntil minutes"
                        timeUntil < 1440 -> "${timeUntil / 60} hours"
                        else -> "${timeUntil / 1440} days"
                    }
                    Text(
                        text = "Reminder in $formattedTime",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                TextButton(onClick = onClick) {
                    Text("View Task")
                }
            }
            Text(
                text = "Due: ${task.deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
} 