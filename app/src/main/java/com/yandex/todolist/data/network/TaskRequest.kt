// data/network/TaskRequest.kt
package com.yandex.todolist.data.network

import com.yandex.todolist.domain.model.Importance

data class TaskRequest(
    val id: Int,
    val description: String,
    val importance: Importance,
    val deadline: Long?,
    val isDone: Boolean
)
