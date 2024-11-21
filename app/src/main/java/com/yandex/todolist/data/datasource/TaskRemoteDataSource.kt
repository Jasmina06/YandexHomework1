package com.yandex.todolist.data.datasource


import com.yandex.todolist.data.mappers.toDomainModel
import com.yandex.todolist.data.mappers.toRequestModel
import com.yandex.todolist.data.network.ApiService
import com.yandex.todolist.domain.model.Task

class TaskRemoteDataSource(private val apiService: ApiService) {

    suspend fun fetchTasks(): List<Task> {
        return apiService.getTasks().map { it.toDomainModel() }
    }

    suspend fun addTask(task: Task): Task {
        return apiService.addTask(task.toRequestModel()).toDomainModel()
    }

    suspend fun updateTasks(revision: Int, tasks: List<Task>) {
        apiService.updateTasks(revision, tasks.map { it.toRequestModel() })
    }

    suspend fun deleteTask(taskId: Int) {
        apiService.deleteTask(taskId)
    }
}
