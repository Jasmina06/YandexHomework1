package com.yandex.todolist

import TaskEditorScreen
import ToDoAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.todolist.presentation.ui.TaskListScreen
import com.yandex.todolist.presentation.viewmodel.TaskViewModelFactory
import com.yandex.todolist.data.repository.TaskRepositoryImpl
import com.yandex.todolist.domain.usecase.AddTaskUseCase
import com.yandex.todolist.domain.usecase.DeleteTaskUseCase
import com.yandex.todolist.domain.usecase.GetTasksUseCase
import com.yandex.todolist.domain.usecase.UpdateTaskUseCase
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize repository and use cases
        val taskRepository = TaskRepositoryImpl()
        val getTasksUseCase = GetTasksUseCase(taskRepository)
        val addTaskUseCase = AddTaskUseCase(taskRepository)
        val updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)

        // Initialize ViewModel factory
        val factory = TaskViewModelFactory(
            getTasksUseCase,
            addTaskUseCase,
            updateTaskUseCase,
            deleteTaskUseCase
        )

        // Create ViewModel using factory
        viewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        // Устанавливаем цвет статус-бара и навигационной панели
        setStatusBarAndNavBarColors()

        // Set the content with MyApp composable
        setContent {
            ToDoAppTheme {
                MyApp(viewModel = viewModel)
            }
        }
    }

    private fun setStatusBarAndNavBarColors() {
        val window = this.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        val isLightTheme = !isSystemInDarkTheme() // Проверяем, тёмная тема или нет

        // Устанавливаем цвета на основе активной темы
        window.statusBarColor = if (isLightTheme) Color.White.toArgb() else Color.Black.toArgb()
        window.navigationBarColor = if (isLightTheme) Color.White.toArgb() else Color.Black.toArgb()

        // Устанавливаем цвет иконок статус-бара и навигационной панели
        insetsController.isAppearanceLightStatusBars = isLightTheme
        insetsController.isAppearanceLightNavigationBars = isLightTheme
    }

    private fun isSystemInDarkTheme(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}

@Composable
fun MyApp(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    // Define navigation host with routes
    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            TaskListScreen(
                viewModel = viewModel,
                onAddTask = { navController.navigate("taskEditor") },
                onEditTask = { task -> navController.navigate("taskEditor?taskId=${task.id}") }
            )
        }
        composable("taskEditor?taskId={taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            TaskEditorScreen(
                viewModel = viewModel,
                taskId = taskId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
