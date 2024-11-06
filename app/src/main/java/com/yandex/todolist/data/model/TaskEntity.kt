package com.yandex.todolist.data.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val description: String,
    val importance: String = "Нет",
    val deadline: LocalDate? = null,
    val isDone: Boolean = false
)
