package com.yandex.todolist.data.mappers

import com.yandex.todolist.data.model.TaskEntity
import com.yandex.todolist.data.network.TaskRequest
import com.yandex.todolist.data.network.TaskResponse
import com.yandex.todolist.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun TaskEntity.toDomainModel(): Task {
    return Task(
        id = id,
        description = description,
        importance = importance,
        deadline = deadline,
        isDone = isDone
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        description = description,
        importance = importance,
        deadline = deadline,
        isDone = isDone
    )
}
fun Task.toRequestModel(): TaskRequest {
    return TaskRequest(
        id = id,
        description = description,
        importance = importance,
        deadline = deadline?.atStartOfDay(ZoneId.systemDefault())?.toEpochSecond(),
        isDone = isDone
    )
}

fun TaskResponse.toDomainModel(): Task {
    return Task(
        id = id,
        description = description,
        importance = importance,
        deadline = deadline?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate() },
        isDone = isDone
    )
}