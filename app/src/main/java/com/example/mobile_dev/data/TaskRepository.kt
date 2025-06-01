package com.example.mobile_dev.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneOffset

class TaskRepository(private val taskDao: TaskDao) {
    val pendingTasks: Flow<List<Task>> = taskDao.getPendingTasks()
    val completedTasks: Flow<List<Task>> = taskDao.getCompletedTasks()
    val taskGroups: Flow<List<TaskGroup>> = taskDao.getAllTaskGroups()

    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>> =
        taskDao.getTasksByStatus(status)

    fun getTasksByGroup(groupId: Long): Flow<List<Task>> =
        taskDao.getTasksByGroup(groupId)

    fun getUpcomingReminders(): Flow<List<Task>> =
        taskDao.getUpcomingReminders(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTaskStatus(taskId: Long, status: TaskStatus) {
        taskDao.updateTaskStatus(
            taskId,
            status,
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    suspend fun toggleTaskCompletion(taskId: Long, completed: Boolean) {
        val status = if (completed) TaskStatus.COMPLETED else TaskStatus.TODO
        taskDao.updateTaskCompletion(
            taskId,
            completed,
            status,
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    suspend fun updateTaskReminder(taskId: Long, reminderTime: LocalDateTime?) {
        taskDao.updateTaskReminder(
            taskId,
            reminderTime?.toEpochSecond(ZoneOffset.UTC)
        )
    }

    // Task Group operations
    suspend fun insertTaskGroup(taskGroup: TaskGroup): Long {
        return taskDao.insertTaskGroup(taskGroup)
    }

    suspend fun updateTaskGroup(taskGroup: TaskGroup) {
        taskDao.updateTaskGroup(taskGroup)
    }

    suspend fun deleteTaskGroup(taskGroup: TaskGroup) {
        taskDao.deleteTaskGroup(taskGroup)
    }

    suspend fun getTaskGroupById(groupId: Long): TaskGroup? {
        return taskDao.getTaskGroupById(groupId)
    }
} 