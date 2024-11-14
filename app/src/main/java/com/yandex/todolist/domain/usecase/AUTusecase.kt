package com.yandex.todolist.domain.usecase

import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.addTask(task)
}

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Int) = repository.deleteTask(taskId)
}

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
}
