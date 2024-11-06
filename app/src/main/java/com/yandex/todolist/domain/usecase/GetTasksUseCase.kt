package com.yandex.todolist.domain.usecase

import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
}
