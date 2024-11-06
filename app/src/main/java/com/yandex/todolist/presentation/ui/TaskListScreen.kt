package com.yandex.todolist.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yandex.todolist.R
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import java.time.LocalDate

@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var showCompleted by remember { mutableStateOf(true) }
    var showTaskEditor by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    val completedTasksCount = tasks.count { it.isDone }
    val filteredTasks = tasks.filter { !it.isDone || showCompleted }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with title and visibility toggle button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.my_tasks),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showCompleted = !showCompleted }) {
                    Icon(
                        imageVector = if (showCompleted) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (showCompleted) stringResource(id = R.string.hide_completed) else stringResource(id = R.string.show_completed),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Display completed tasks count
            Text(
                text = stringResource(id = R.string.completed_tasks, completedTasksCount),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Task list
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = { isChecked ->
                            viewModel.updateTask(task.copy(isDone = isChecked))
                        },
                        onDelete = { viewModel.deleteTask(task.id) },
                        onEdit = {
                            taskToEdit = it
                            showTaskEditor = true
                        }
                    )
                }
            }
        }

        // Floating Action Button for adding new tasks
        FloatingActionButton(
            onClick = {
                taskToEdit = null
                showTaskEditor = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_task))
        }
    }

    // Task editor dialog
    if (showTaskEditor) {
        TaskEditorDialog(
            task = taskToEdit,
            onDismiss = { showTaskEditor = false },
            onSave = { task ->
                if (task.id == 0) { // Assuming id = 0 for new tasks
                    viewModel.addTask(task.copy(id = tasks.size + 1))
                } else {
                    viewModel.updateTask(task)
                }
                showTaskEditor = false
            }
        )
    }
}

@Composable
fun TaskEditorDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var description by remember { mutableStateOf(task?.description ?: "") }
    var importance by remember { mutableStateOf(task?.importance ?: "Нет") }
    var deadlineEnabled by remember { mutableStateOf(task?.deadline != null) }
    val deadline by remember { mutableStateOf(task?.deadline ?: LocalDate.now()) }
    var importanceExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (task == null) stringResource(R.string.new_task) else stringResource(R.string.edit_task),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Task description input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.task_description)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Importance dropdown
                Text(text = stringResource(R.string.importance))
                Box {
                    OutlinedButton(
                        onClick = { importanceExpanded = !importanceExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = importance)
                    }

                    DropdownMenu(
                        expanded = importanceExpanded,
                        onDismissRequest = { importanceExpanded = false }
                    ) {
                        listOf("Нет", "Высокая").forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    importance = level
                                    importanceExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Deadline switch and date button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.deadline))
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(checked = deadlineEnabled, onCheckedChange = { deadlineEnabled = it })
                }

                if (deadlineEnabled) {
                    Button(onClick = {
                        // Date selection logic
                    }) {
                        Text(deadline.toString())
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save and Cancel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        onSave(
                            Task(
                                id = task?.id ?: 0,
                                description = description,
                                importance = importance,
                                deadline = if (deadlineEnabled) deadline else null,
                                isDone = task?.isDone ?: false
                            )
                        )
                    }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit, onEdit: (Task) -> Unit) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val backgroundColor = if (task.isDone) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX > 100) {
                        onCheckedChange(true)
                        offsetX = 0f
                    } else if (offsetX < -100) {
                        onDelete()
                        offsetX = 0f
                    }
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            IconButton(onClick = { onEdit(task) }) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = stringResource(id = R.string.edit_task),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_task),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
