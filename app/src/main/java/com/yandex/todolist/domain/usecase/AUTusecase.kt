package com.yandex.todolist.domain.usecase

import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.repository.TaskRepository

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addTask(task)
}

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Int) = repository.deleteTask(taskId)
}
