package com.yandex.todolist.data.model

import com.yandex.todolist.domain.model.Importance
import java.time.LocalDate

data class Task(
    val id: Int,
    val description: String,
    val importance: Importance = Importance.NONE,
    val deadline: LocalDate? = null,
    val isDone: Boolean = false
)
