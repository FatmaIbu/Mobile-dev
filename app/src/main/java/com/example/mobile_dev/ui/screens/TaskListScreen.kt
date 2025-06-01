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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.Task
import com.example.mobile_dev.ui.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    groupId: Long,
    onBackClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit
) {
    val tasks by viewModel.getTasksByGroup(groupId).collectAsState(initial = emptyList())
    val group by remember(groupId) {
        derivedStateOf {
            viewModel.taskGroups.value.find { it.id == groupId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(group?.name ?: "Tasks")
                        if (group?.description?.isNotBlank() == true) {
                            Text(
                                text = group?.description ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddTaskClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            )
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            EmptyState(
                title = "No Tasks in Group",
                message = "Add tasks to ${group?.name ?: "this group"} to get started",
                image = R.drawable.empty_tasks,
                actionLabel = "Add Task",
                onAction = onAddTaskClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(
                    items = tasks.sortedWith(
                        compareBy<Task> { it.status }
                            .thenBy { it.priority }
                            .thenBy { it.deadline }
                    )
                ) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = { onTaskClick(task.id) },
                        onStatusChange = {
                            when (task.status) {
                                com.example.mobile_dev.data.TaskStatus.TODO ->
                                    viewModel.updateTaskStatus(task.id, com.example.mobile_dev.data.TaskStatus.IN_PROGRESS)
                                com.example.mobile_dev.data.TaskStatus.IN_PROGRESS ->
                                    viewModel.updateTaskStatus(task.id, com.example.mobile_dev.data.TaskStatus.COMPLETED)
                                com.example.mobile_dev.data.TaskStatus.COMPLETED ->
                                    viewModel.updateTaskStatus(task.id, com.example.mobile_dev.data.TaskStatus.TODO)
                            }
                        }
                    )
                }
            }
        }
    }
} 