import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yandex.todolist.domain.model.Task
import com.yandex.todolist.domain.model.Importance
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import java.time.LocalDate
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    viewModel: TaskViewModel,
    taskId: Int?,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val task = taskId?.let { id -> viewModel.tasks.value.find { it.id == id } }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var importance by remember { mutableStateOf(task?.importance ?: Importance.NONE) }
    var deadlineEnabled by remember { mutableStateOf(task?.deadline != null) }
    var deadline by remember { mutableStateOf(task?.deadline ?: LocalDate.now()) }
    var importanceExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (taskId == null) "Новая задача" else "Редактировать задачу") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Отменить")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val newTask = Task(
                            id = task?.id ?: (viewModel.tasks.value.size + 1),
                            description = description,
                            importance = importance,
                            deadline = if (deadlineEnabled) deadline else null,
                            isDone = task?.isDone ?: false
                        )

                        if (taskId == null) {
                            viewModel.addTask(newTask)
                            Log.d("TaskEditorScreen", "New task added: $newTask")
                        } else {
                            viewModel.updateTask(newTask)
                            Log.d("TaskEditorScreen", "Task updated: $newTask")
                        }
                        onSave()
                    }) {
                        Text(text = "Сохранить", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Что нужно сделать...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(text = "Важность", color = MaterialTheme.colorScheme.primary)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { importanceExpanded = !importanceExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = importance.displayName)
                    }
                    DropdownMenu(
                        expanded = importanceExpanded,
                        onDismissRequest = { importanceExpanded = false }
                    ) {
                        Importance.values().forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level.displayName) },
                                onClick = {
                                    importance = level
                                    importanceExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Крайний срок", color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = deadlineEnabled,
                        onCheckedChange = { deadlineEnabled = it }
                    )
                }

                if (deadlineEnabled) {
                    OutlinedButton(
                        onClick = {
                            showDatePickerDialog(context) { selectedDate ->
                                deadline = selectedDate
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = deadline.toString())
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        if (task != null) {
                            viewModel.deleteTask(task.id)
                            onCancel()
                        }
                    },
                    enabled = task != null,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Удалить", color = Color.Red)
                }
            }
        }
    )
}

fun showDatePickerDialog(context: Context, onDateSelected: (LocalDate) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
