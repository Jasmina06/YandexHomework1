package com.yandex.todolist.domain.model


data class Task(
    val id: Int,
    val description: String,
    val isDone: Boolean
)
