package com.yandex.todolist.data.repository

import com.yandex.todolist.data.datasource.TaskLocalDataSource
import com.yandex.todolist.data.datasource.TaskRemoteDataSource
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import java.io.IOException

class TaskRepositoryImpl(
    private val remoteDataSource: TaskRemoteDataSource,
    private val localDataSource: TaskLocalDataSource
) : TaskRepository {

    private var revision: Int = 0

    override fun getTasks(): Flow<List<Task>> = flow {
        try {
            val tasksFromApi = remoteDataSource.fetchTasks()
            revision += 1
            localDataSource.saveTasks(tasksFromApi)
            emit(tasksFromApi)
        } catch (e: Exception) {
            emitAll(localDataSource.getAllTasks())
            throw e
        }
    }.retry(3) { delay(2000); true }
        .catch { emitAll(localDataSource.getAllTasks()) }

    override suspend fun addTask(task: Task) {
        try {
            val addedTask = remoteDataSource.addTask(task)
            localDataSource.addTask(addedTask)
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при добавлении задачи")
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            remoteDataSource.updateTasks(revision, listOf(task))
            localDataSource.updateTask(task)
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при обновлении задачи")
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        try {
            remoteDataSource.deleteTask(taskId)
            localDataSource.deleteTask(taskId)
            revision += 1
        } catch (e: Exception) {
            throw IOException("Ошибка при удалении задачи")
        }
    }
}
