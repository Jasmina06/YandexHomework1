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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import android.util.Log
import kotlinx.coroutines.flow.retry

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            Log.d("TaskViewModel", "Fetching tasks started")
            _isRefreshing.value = true
            getTasksUseCase()
                .onStart {
                    _errorMessage.value = null
                    Log.d("TaskViewModel", "Loading started, error cleared")
                }
                .retry(3) { e ->
                    delay(2000)
                    Log.d("TaskViewModel", "Retry due to error: ${e.localizedMessage}")
                    e is IOException || e is HttpException
                }
                .catch { e ->
                    _errorMessage.value = e.localizedMessage ?: "Произошла ошибка при загрузке задач"
                    _isRefreshing.value = false
                    Log.e("TaskViewModel", "Error during fetch: ${e.localizedMessage}")
                }
                .onCompletion {
                    _isRefreshing.value = false
                    Log.d("TaskViewModel", "Fetching tasks completed, refreshing stopped")
                }
                .collect { taskList ->
                    _tasks.value = taskList
                    _errorMessage.value = null
                    Log.d("TaskViewModel", "Tasks fetched successfully: ${taskList.size} items")
                }
        }
    }

    fun refreshTasks() {
        fetchTasks()
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                addTaskUseCase(task)
                _tasks.value = _tasks.value + task
                Log.d("TaskViewModel", "Task added successfully: $task")
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка при добавлении задачи: ${e.localizedMessage}"
                Log.e("TaskViewModel", "Error adding task: ${e.localizedMessage}")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                updateTaskUseCase(task)
                _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
                Log.d("TaskViewModel", "Task updated successfully: $task")
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка при обновлении задачи: ${e.localizedMessage}"
                Log.e("TaskViewModel", "Error updating task: ${e.localizedMessage}")
            }
        }
    }
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId)
                _tasks.value = _tasks.value.filterNot { it.id == taskId }
                Log.d("TaskViewModel", "Task deleted successfully: ID $taskId")
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка при удалении задачи: ${e.localizedMessage}"
                Log.e("TaskViewModel", "Error deleting task: ${e.localizedMessage}")
            }
        }
    }
}