package com.example.mobile_dev.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY deadline ASC")
    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE groupId = :groupId ORDER BY deadline ASC")
    fun getTasksByGroup(groupId: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY deadline ASC")
    fun getPendingTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY updatedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE reminderTime IS NOT NULL AND reminderTime > :now ORDER BY reminderTime ASC")
    fun getUpcomingReminders(now: Long): Flow<List<Task>>

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET status = :status, updatedAt = :timestamp WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, status: TaskStatus, timestamp: Long)

    @Query("UPDATE tasks SET isCompleted = :completed, status = :status, updatedAt = :timestamp WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Long, completed: Boolean, status: TaskStatus, timestamp: Long)

    @Query("UPDATE tasks SET reminderTime = :reminderTime WHERE id = :taskId")
    suspend fun updateTaskReminder(taskId: Long, reminderTime: Long?)

    // Task Group operations
    @Insert
    suspend fun insertTaskGroup(taskGroup: TaskGroup): Long

    @Update
    suspend fun updateTaskGroup(taskGroup: TaskGroup)

    @Delete
    suspend fun deleteTaskGroup(taskGroup: TaskGroup)

    @Query("SELECT * FROM task_groups ORDER BY name ASC")
    fun getAllTaskGroups(): Flow<List<TaskGroup>>

    @Query("SELECT * FROM task_groups WHERE id = :groupId")
    suspend fun getTaskGroupById(groupId: Long): TaskGroup?
} 