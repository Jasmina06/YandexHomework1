// data/repository/TaskRepositoryImpl.kt
package com.yandex.todolist.data.repository

import com.yandex.todolist.data.local.TaskDao
import com.yandex.todolist.data.mappers.toDomainModel
import com.yandex.todolist.data.mappers.toEntity
import com.yandex.todolist.data.mappers.toRequestModel
import com.yandex.todolist.data.network.ApiService
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException

class TaskRepositoryImpl(
    private val apiService: ApiService,
    private val taskDao: TaskDao
) : TaskRepository {

    private var revision: Int = 0

    override fun getTasks(): Flow<List<Task>> = flow {
        try {
            val tasksFromApi = apiService.getTasks().map { it.toDomainModel() }
            revision += 1
            taskDao.clearAllTasks()
            tasksFromApi.forEach { task -> taskDao.insertTask(task.toEntity()) }
            emit(tasksFromApi)
        } catch (e: Exception) {
            emitAll(taskDao.getAllTasks().map { it.map { entity -> entity.toDomainModel() } })
            throw e
        }
    }.retry(3) { delay(2000); true }
        .catch { emitAll(taskDao.getAllTasks().map { it.map { entity -> entity.toDomainModel() } }) }

    override suspend fun addTask(task: Task) {
        try {
            val response = apiService.addTask(task.toRequestModel())
            taskDao.insertTask(response.toDomainModel().toEntity())
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при добавлении задачи")
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            apiService.updateTasks(revision = revision, tasks = listOf(task.toRequestModel()))
            taskDao.updateTask(task.toEntity())
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при обновлении задачи")
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        try {
            apiService.deleteTask(taskId)
            taskDao.deleteTask(taskId)
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при удалении задачи")
        }
    }
}
