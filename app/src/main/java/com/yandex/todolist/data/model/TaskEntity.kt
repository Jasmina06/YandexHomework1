package com.yandex.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yandex.todolist.domain.model.Importance
import java.time.LocalDate

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val importance: Importance = Importance.NONE,
    val deadline: LocalDate? = null,
    val isDone: Boolean = false
)
