package com.yandex.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.yandex.todolist.presentation.viewmodel.TaskViewModelFactory
import com.yandex.todolist.data.repository.TaskRepositoryImpl
import com.yandex.todolist.domain.usecase.AddTaskUseCase
import com.yandex.todolist.domain.usecase.DeleteTaskUseCase
import com.yandex.todolist.domain.usecase.GetTasksUseCase
import com.yandex.todolist.domain.usecase.UpdateTaskUseCase
import com.yandex.todolist.presentation.ui.TaskListScreen
import com.yandex.todolist.presentation.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskRepository = TaskRepositoryImpl()
        val getTasksUseCase = GetTasksUseCase(taskRepository)
        val addTaskUseCase = AddTaskUseCase(taskRepository)
        val updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)

        val factory = TaskViewModelFactory(
            getTasksUseCase,
            addTaskUseCase,
            updateTaskUseCase,
            deleteTaskUseCase
        )

        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        setContent {
            TaskListScreen(viewModel = viewModel)
        }
    }
}
