// data/repository/TaskRepositoryImpl.kt
package com.yandex.todolist.data.repository


import com.yandex.todolist.data.model.Task
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TaskRepositoryImpl : TaskRepository {
    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getTasks(): Flow<List<Task>> = tasks

    override suspend fun addTask(task: Task) {
        tasks.value += task
    }

    override suspend fun updateTask(task: Task) {
        tasks.value = tasks.value.map {
            if (it.id == task.id) task else it
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        tasks.value = tasks.value.filter { it.id != taskId }
    }
}
