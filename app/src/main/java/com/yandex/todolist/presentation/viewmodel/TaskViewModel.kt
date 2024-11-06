// TaskViewModel.kt
package com.yandex.todolist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.usecase.AddTaskUseCase
import com.yandex.todolist.domain.usecase.DeleteTaskUseCase
import com.yandex.todolist.domain.usecase.GetTasksUseCase
import com.yandex.todolist.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        fetchTasks() // Инициализация получения задач
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            getTasksUseCase().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
            fetchTasks() // Обновляем список задач
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            fetchTasks() // Обновляем список задач
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
            fetchTasks() // Обновляем список задач
        }
    }
}
