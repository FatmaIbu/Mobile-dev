package com.example.mobile_dev.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.TaskGroup
import com.example.mobile_dev.ui.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskGroupScreen(
    viewModel: TaskViewModel,
    onBackClick: () -> Unit,
    onGroupClick: (Long) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val taskGroups by viewModel.taskGroups.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Groups") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Group")
                    }
                }
            )
        }
    ) { padding ->
        if (taskGroups.isEmpty()) {
            EmptyState(
                title = "No Task Groups",
                message = "Create groups to organize your tasks better",
                image = R.drawable.empty_groups,
                actionLabel = "Create Group",
                onAction = { showAddDialog = true }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(taskGroups) { group ->
                    TaskGroupItem(
                        group = group,
                        onClick = { onGroupClick(group.id) },
                        onDelete = { viewModel.deleteTaskGroup(group) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditGroupDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, description, color ->
                viewModel.createTaskGroup(name, description, color)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskGroupItem(
    group: TaskGroup,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(group.color ?: MaterialTheme.colorScheme.primary.hashCode()))
                )
                Column {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (group.description.isNotBlank()) {
                        Text(
                            text = group.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Group")
            }
        }
    }
}

@Composable
fun AddEditGroupDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, color: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Red.hashCode()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Task Group") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Group Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Color.Red,
                        Color.Green,
                        Color.Blue,
                        Color.Yellow,
                        Color.Magenta,
                        Color.Cyan
                    ).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color.hashCode() }
                                .then(
                                    if (selectedColor == color.hashCode())
                                        Modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                    else Modifier
                                )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, description, selectedColor)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 