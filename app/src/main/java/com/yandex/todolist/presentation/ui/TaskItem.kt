package com.yandex.todolist.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.model.Importance
import java.time.format.DateTimeFormatter

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: (Task) -> Unit
) {
    // Формат даты для отображения дедлайна
    val deadlineText = task.deadline?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "Нет дедлайна"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Чекбокс для статуса выполнения задачи
        Checkbox(
            checked = task.isDone,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Отображение описания задачи
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Отображение уровня важности задачи, если он не равен NONE
            if (task.importance != Importance.NONE) {
                Text(
                    text = "Важность: ${task.importance.displayName}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (task.importance == Importance.HIGH) Color.Red else MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    fontSize = 14.sp
                )
            }

            // Отображение дедлайна, если он задан
            if (task.deadline != null) {
                Text(
                    text = "Крайний срок: $deadlineText",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Кнопка редактирования задачи
        IconButton(onClick = { onEdit(task) }) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Редактировать задачу",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Кнопка удаления задачи
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Удалить задачу",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
