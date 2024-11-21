package com.yandex.todolist.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yandex.todolist.R
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showCompleted by remember { mutableStateOf(true) }
    val completedTasksCount = tasks.count { it.isDone }
    val filteredTasks = tasks.filter { !it.isDone || showCompleted }
    val snackbarHostState = remember { SnackbarHostState() }

    // Получаем строки из stringResource заранее
    val errorMessage = stringResource(R.string.error_message)
    val retryLabel = stringResource(R.string.retry)
    val myTasksTitle = stringResource(id = R.string.my_tasks)
    val newTaskLabel = stringResource(id = R.string.new_task)
    val addTaskDescription = stringResource(id = R.string.add_task)

    // Логируем отфильтрованные задачи
    Log.d("TaskListScreen", "Showing tasks: ${filteredTasks.size} (completed shown: $showCompleted)")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = myTasksTitle,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    showCompleted = !showCompleted
                    Log.d("TaskListScreen", "Show completed toggled: $showCompleted")
                }) {
                    Icon(
                        imageVector = if (showCompleted) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (showCompleted) stringResource(R.string.hide_completed_tasks) else stringResource(R.string.show_completed_tasks),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.completed_tasks, completedTasksCount),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = viewModel.isRefreshing.collectAsState().value),
                onRefresh = {
                    Log.d("TaskListScreen", "Refreshing tasks")
                    viewModel.refreshTasks()
                }
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { isChecked ->
                                Log.d("TaskListScreen", "Task ${task.id} checked changed to $isChecked")
                                viewModel.updateTask(task.copy(isDone = isChecked))
                            },
                            onDelete = {
                                Log.d("TaskListScreen", "Task ${task.id} deleted")
                                viewModel.deleteTask(task.id)
                            },
                            onEdit = { onEditTask(task) }
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                                .clickable(onClick = onAddTask)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = newTaskLabel,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                Log.d("TaskListScreen", "FloatingActionButton clicked")
                onAddTask()
            },
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = addTaskDescription)
        }

        LaunchedEffect(Unit) {
            viewModel.errorMessage.collect { error ->
                if (error != null) {
                    Log.e("TaskListScreen", "Error message received: $error")
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = errorMessage,
                            actionLabel = retryLabel
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            Log.d("TaskListScreen", "Retry action performed")
                            viewModel.refreshTasks()
                        }
                    }
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}
