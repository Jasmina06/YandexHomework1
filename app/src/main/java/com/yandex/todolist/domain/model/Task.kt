// Task.kt
package com.yandex.todolist.domain.model

import java.time.LocalDate

// Модель задачи
data class Task(
    val id: Int,                           // Уникальный идентификатор задачи
    val description: String,               // Описание задачи
    val importance: Importance = Importance.NONE,  // Уровень важности
    val deadline: LocalDate? = null,       // Крайний срок выполнения
    val isDone: Boolean = false            // Статус выполнения задачи
)
