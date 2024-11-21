package com.yandex.todolist.domain.model

enum class Importance(val displayName: String) {
    NONE("Нет"),
    LOW("Низкая"),
    HIGH("Высокая");

    override fun toString(): String {
        return displayName
    }
}