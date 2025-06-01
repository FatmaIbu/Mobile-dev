package com.example.mobile_dev.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobile_dev.data.PreferencesManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()
    val isDarkTheme by preferencesManager.isDarkTheme.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "Appearance") {
                    SettingsSwitch(
                        title = "Dark Theme",
                        subtitle = "Use dark theme throughout the app",
                        icon = Icons.Default.DarkMode,
                        checked = isDarkTheme,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                preferencesManager.setDarkTheme(isChecked)
                            }
                        }
                    )
                }
            }

            item {
                SettingsSection(title = "Notifications") {
                    SettingsSwitch(
                        title = "Task Reminders",
                        subtitle = "Get notified about upcoming tasks",
                        icon = Icons.Default.Notifications,
                        checked = true,
                        onCheckedChange = { }
                    )
                    SettingsSwitch(
                        title = "Due Date Alerts",
                        subtitle = "Get notified when tasks are due soon",
                        icon = Icons.Default.AccessTime,
                        checked = true,
                        onCheckedChange = { }
                    )
                }
            }

            item {
                SettingsSection(title = "Task Management") {
                    SettingsSwitch(
                        title = "Auto-archive Completed",
                        subtitle = "Automatically archive completed tasks after 30 days",
                        icon = Icons.Default.Archive,
                        checked = false,
                        onCheckedChange = { }
                    )
                    SettingsSwitch(
                        title = "Show Task Statistics",
                        subtitle = "Display task completion statistics",
                        icon = Icons.Default.BarChart,
                        checked = true,
                        onCheckedChange = { }
                    )
                }
            }

            item {
                SettingsSection(title = "About") {
                    SettingsItem(
                        title = "Version",
                        subtitle = "1.0.0",
                        icon = Icons.Default.Info
                    )
                    SettingsItem(
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        icon = Icons.Default.Security,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsSwitch(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(icon, contentDescription = null)
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (onClick != null) {
                    clickable(onClick = onClick)
                } else {
                    this
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        if (onClick != null) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Open")
        }
    }
} 