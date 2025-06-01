package com.example.mobile_dev.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_dev.ui.screens.*
import com.example.mobile_dev.ui.TaskViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CompletedTasks : Screen("completed_tasks")
    object AddEditTask : Screen("add_edit_task?taskId={taskId}") {
        fun createRoute(taskId: Long? = null) = "add_edit_task${taskId?.let { "?taskId=$it" } ?: ""}"
    }
    object Settings : Screen("settings")
    object TaskGroups : Screen("task_groups")
    object GroupTasks : Screen("group_tasks/{groupId}") {
        fun createRoute(groupId: Long) = "group_tasks/$groupId"
    }
    object Reminders : Screen("reminders")
}

@Composable
fun TaskNavigation(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onTaskClick = { taskId ->
                    navController.navigate(Screen.AddEditTask.createRoute(taskId))
                },
                onAddTaskClick = {
                    navController.navigate(Screen.AddEditTask.createRoute())
                },
                onCompletedTasksClick = {
                    navController.navigate(Screen.CompletedTasks.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.CompletedTasks.route) {
            CompletedTasksScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screen.AddEditTask.route
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            AddEditTaskScreen(
                taskId = taskId,
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.TaskGroups.route) {
            TaskGroupScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onGroupClick = { groupId ->
                    navController.navigate(Screen.GroupTasks.createRoute(groupId))
                }
            )
        }

        composable(
            route = Screen.GroupTasks.route
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toLongOrNull()
            if (groupId != null) {
                TaskListScreen(
                    viewModel = viewModel,
                    groupId = groupId,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onTaskClick = { taskId ->
                        navController.navigate(Screen.AddEditTask.createRoute(taskId))
                    },
                    onAddTaskClick = {
                        navController.navigate(Screen.AddEditTask.createRoute())
                    }
                )
            }
        }

        composable(Screen.Reminders.route) {
            ReminderScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.AddEditTask.createRoute(taskId))
                }
            )
        }
    }
} 