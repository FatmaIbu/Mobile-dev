package com.example.mobile_dev.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.Task
import com.example.mobile_dev.data.TaskStatus
import com.example.mobile_dev.ui.TaskViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    onCompletedTasksClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val todoTasks by viewModel.todoTasks.collectAsState()
    val inProgressTasks by viewModel.inProgressTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Management") },
                actions = {
                    IconButton(onClick = onCompletedTasksClick) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Completed Tasks")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("To Do") },
                    icon = { Icon(Icons.Default.List, "To Do") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("In Progress") },
                    icon = { Icon(Icons.Default.Pending, "In Progress") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Completed") },
                    icon = { Icon(Icons.Default.Done, "Completed") }
                )
            }

            when (selectedTab) {
                0 -> TaskList(
                    tasks = todoTasks,
                    emptyMessage = "No tasks to do",
                    onTaskClick = onTaskClick,
                    onStatusChange = { taskId ->
                        viewModel.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS)
                    }
                )
                1 -> TaskList(
                    tasks = inProgressTasks,
                    emptyMessage = "No tasks in progress",
                    onTaskClick = onTaskClick,
                    onStatusChange = { taskId ->
                        viewModel.updateTaskStatus(taskId, TaskStatus.COMPLETED)
                    }
                )
                2 -> TaskList(
                    tasks = completedTasks,
                    emptyMessage = "No completed tasks",
                    onTaskClick = onTaskClick,
                    onStatusChange = { taskId ->
                        viewModel.updateTaskStatus(taskId, TaskStatus.TODO)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    emptyMessage: String,
    onTaskClick: (Long) -> Unit,
    onStatusChange: (Long) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyState(
            title = "No Tasks",
            message = emptyMessage,
            image = R.drawable.empty_tasks,
            actionLabel = "Add Task",
            onAction = onTaskClick
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onTaskClick = { onTaskClick(task.id) },
                    onStatusChange = { onStatusChange(task.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onStatusChange: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onTaskClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssignedIcon(task.status)
                    Text(
                        text = "Due: ${task.deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    PriorityChip(task)
                }
            }
            IconButton(onClick = onStatusChange) {
                Icon(
                    when (task.status) {
                        TaskStatus.TODO -> Icons.Default.ArrowForward
                        TaskStatus.IN_PROGRESS -> Icons.Default.Done
                        TaskStatus.COMPLETED -> Icons.Default.Refresh
                    },
                    contentDescription = "Change Status"
                )
            }
        }
    }
}

@Composable
fun AssignedIcon(status: TaskStatus) {
    Icon(
        imageVector = when (status) {
            TaskStatus.TODO -> Icons.Default.List
            TaskStatus.IN_PROGRESS -> Icons.Default.Pending
            TaskStatus.COMPLETED -> Icons.Default.Done
        },
        contentDescription = status.name,
        tint = when (status) {
            TaskStatus.TODO -> Color.Gray
            TaskStatus.IN_PROGRESS -> Color.Blue
            TaskStatus.COMPLETED -> Color.Green
        },
        modifier = Modifier.size(16.dp)
    )
}

@Composable
fun PriorityChip(task: Task) {
    Surface(
        color = when (task.priority) {
            com.example.mobile_dev.data.Priority.HIGH -> Color.Red.copy(alpha = 0.1f)
            com.example.mobile_dev.data.Priority.MEDIUM -> Color.Yellow.copy(alpha = 0.1f)
            com.example.mobile_dev.data.Priority.LOW -> Color.Green.copy(alpha = 0.1f)
        },
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = task.priority.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = when (task.priority) {
                com.example.mobile_dev.data.Priority.HIGH -> Color.Red
                com.example.mobile_dev.data.Priority.MEDIUM -> Color.DarkGray
                com.example.mobile_dev.data.Priority.LOW -> Color.Green
            }
        )
    }
} 