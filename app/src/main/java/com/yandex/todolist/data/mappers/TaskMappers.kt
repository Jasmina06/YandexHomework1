package com.yandex.todolist.data.mappers

import com.yandex.todolist.data.model.Task as DataTask
import com.yandex.todolist.domain.model.Task as DomainTask

fun DataTask.toDomainModel(): DomainTask {
    return DomainTask(
        id = this.id,
        description = this.description,
        isDone = this.isDone
    )
}

fun DomainTask.toDataModel(): DataTask {
    return DataTask(
        id = this.id,
        description = this.description,
        isDone = this.isDone
    )
}
