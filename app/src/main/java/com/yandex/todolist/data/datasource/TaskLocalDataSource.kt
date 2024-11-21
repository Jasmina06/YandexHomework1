package com.yandex.todolist.data.datasource

import com.yandex.todolist.data.local.TaskDao
import com.yandex.todolist.data.mappers.toDomainModel
import com.yandex.todolist.data.mappers.toEntity
import com.yandex.todolist.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLocalDataSource(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { tasks -> tasks.map { it.toDomainModel() } }
    }

    suspend fun saveTasks(tasks: List<Task>) {
        taskDao.clearAllTasks()
        tasks.forEach { task -> taskDao.insertTask(task.toEntity()) }
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }
}
