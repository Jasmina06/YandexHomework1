package com.yandex.todolist.data.repository

import com.yandex.todolist.data.model.Task as DataTask
import com.yandex.todolist.data.mappers.toDataModel
import com.yandex.todolist.data.mappers.toDomainModel
import com.yandex.todolist.domain.model.Task as DomainTask
import com.yandex.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl : TaskRepository {
    private val tasks = MutableStateFlow<List<DataTask>>(emptyList())

    override fun getTasks(): Flow<List<DomainTask>> {
        return tasks.map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun addTask(task: DomainTask) {
        tasks.value = tasks.value + task.toDataModel()
    }

    override suspend fun updateTask(task: DomainTask) {
        tasks.value = tasks.value.map { existingTask ->
            if (existingTask.id == task.id) task.toDataModel() else existingTask
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        tasks.value = tasks.value.filter { it.id != taskId }
    }
}
