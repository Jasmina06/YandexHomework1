package com.yandex.todolist.data.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val description: String,
    val importance: String = "Нет",  // Добавлено значение по умолчанию
    val deadline: LocalDate? = null, // Поле для дедлайна, может быть null
    val isDone: Boolean = false
)
