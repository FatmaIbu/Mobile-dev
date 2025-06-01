package com.example.mobile_dev.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _selectedStatus = MutableStateFlow(TaskStatus.TODO)
    val selectedStatus: StateFlow<TaskStatus> = _selectedStatus

    private val _selectedGroupId = MutableStateFlow<Long?>(null)
    val selectedGroupId: StateFlow<Long?> = _selectedGroupId

    val taskGroups = repository.taskGroups.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val todoTasks = repository.getTasksByStatus(TaskStatus.TODO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val inProgressTasks = repository.getTasksByStatus(TaskStatus.IN_PROGRESS).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val completedTasks = repository.getTasksByStatus(TaskStatus.COMPLETED).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val upcomingReminders = repository.getUpcomingReminders().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val currentGroupTasks = selectedGroupId.flatMapLatest { groupId ->
        if (groupId != null) {
            repository.getTasksByGroup(groupId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState

    fun setSelectedStatus(status: TaskStatus) {
        _selectedStatus.value = status
    }

    fun setSelectedGroup(groupId: Long?) {
        _selectedGroupId.value = groupId
    }

    fun createTask(
        title: String,
        description: String,
        deadline: LocalDateTime,
        priority: Priority,
        groupId: Long? = null,
        reminderTime: LocalDateTime? = null
    ) {
        if (title.isBlank()) return

        viewModelScope.launch {
            val task = Task(
                title = title,
                description = description,
                deadline = deadline,
                priority = priority,
                groupId = groupId,
                reminderTime = reminderTime
            )
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTaskStatus(taskId: Long, status: TaskStatus) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, status)
        }
    }

    fun toggleTaskCompletion(taskId: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId, completed)
        }
    }

    fun updateTaskReminder(taskId: Long, reminderTime: LocalDateTime?) {
        viewModelScope.launch {
            repository.updateTaskReminder(taskId, reminderTime)
        }
    }

    // Task Group operations
    fun createTaskGroup(name: String, description: String = "", color: Int? = null) {
        if (name.isBlank()) return

        viewModelScope.launch {
            val taskGroup = TaskGroup(
                name = name,
                description = description,
                color = color
            )
            repository.insertTaskGroup(taskGroup)
        }
    }

    fun updateTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            repository.updateTaskGroup(taskGroup)
        }
    }

    fun deleteTaskGroup(taskGroup: TaskGroup) {
        viewModelScope.launch {
            repository.deleteTaskGroup(taskGroup)
        }
    }
}

data class TaskUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 