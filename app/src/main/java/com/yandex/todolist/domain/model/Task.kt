// com/yandex/todolist/domain/model/Task.kt
package com.yandex.todolist.domain.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val description: String,
    val importance: String = "None",  // Default value, if needed
    val deadline: LocalDate? = null,   // Deadline is optional and can be null
    val isDone: Boolean = false
)
