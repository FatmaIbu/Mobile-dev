package com.example.mobile_dev.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.Priority
import com.example.mobile_dev.data.TaskGroup
import com.example.mobile_dev.ui.TaskViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    viewModel: TaskViewModel,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var deadline by remember { mutableStateOf(LocalDateTime.now().plusDays(1)) }
    var reminderTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var selectedGroupId by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showReminderPicker by remember { mutableStateOf(false) }
    var showGroupPicker by remember { mutableStateOf(false) }

    val taskGroups by viewModel.taskGroups.collectAsState()

    LaunchedEffect(taskId) {
        if (taskId != null) {
            // In a real app, you would load the task details here
            // For now, we'll just use empty values
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Priority", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Priority.values().forEach { priorityOption ->
                    FilterChip(
                        selected = priority == priorityOption,
                        onClick = { priority = priorityOption },
                        label = { Text(priorityOption.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")),
                onValueChange = { },
                label = { Text("Deadline") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reminderTime?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
                    ?: "No reminder set",
                onValueChange = { },
                label = { Text("Reminder") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Row {
                        if (reminderTime != null) {
                            IconButton(onClick = { reminderTime = null }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Reminder")
                            }
                        }
                        IconButton(onClick = { showReminderPicker = true }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Set Reminder")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskGroups.find { it.id == selectedGroupId }?.name ?: "No group selected",
                onValueChange = { },
                label = { Text("Group") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Row {
                        if (selectedGroupId != null) {
                            IconButton(onClick = { selectedGroupId = null }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Group")
                            }
                        }
                        IconButton(onClick = { showGroupPicker = true }) {
                            Icon(Icons.Default.Folder, contentDescription = "Select Group")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        if (taskId == null) {
                            viewModel.createTask(
                                title = title,
                                description = description,
                                deadline = deadline,
                                priority = priority,
                                groupId = selectedGroupId,
                                reminderTime = reminderTime
                            )
                        } else {
                            // Update task
                        }
                        onBackClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (taskId == null) "Add Task" else "Update Task")
            }
        }
    }

    if (showGroupPicker) {
        AlertDialog(
            onDismissRequest = { showGroupPicker = false },
            title = { Text("Select Group") },
            text = {
                Column {
                    taskGroups.forEach { group ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedGroupId = group.id
                                    showGroupPicker = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedGroupId == group.id,
                                onClick = { 
                                    selectedGroupId = group.id
                                    showGroupPicker = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(group.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGroupPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // TODO: Implement date picker dialog
    // TODO: Implement time picker dialog
    // TODO: Implement reminder picker dialog
} 