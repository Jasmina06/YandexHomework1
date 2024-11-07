// TaskViewModelFactory.kt
package com.yandex.todolist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yandex.todolist.domain.usecase.AddTaskUseCase
import com.yandex.todolist.domain.usecase.DeleteTaskUseCase
import com.yandex.todolist.domain.usecase.GetTasksUseCase
import com.yandex.todolist.domain.usecase.UpdateTaskUseCase


class TaskViewModelFactory(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(getTasksUseCase, addTaskUseCase, updateTaskUseCase, deleteTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
