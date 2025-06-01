package com.example.mobile_dev.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val priority: Priority,
    val status: TaskStatus = TaskStatus.TODO,
    val groupId: Long? = null,
    val reminderTime: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED
} 