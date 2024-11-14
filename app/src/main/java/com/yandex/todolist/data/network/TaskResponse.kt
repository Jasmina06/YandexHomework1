// data/network/TaskResponse.kt
package com.yandex.todolist.data.network

import com.yandex.todolist.domain.model.Importance

data class TaskResponse(
    val id: Int,
    val description: String,
    val importance: Importance,
    val deadline: Long?,  // Используем timestamp для получения даты
    val isDone: Boolean
)
