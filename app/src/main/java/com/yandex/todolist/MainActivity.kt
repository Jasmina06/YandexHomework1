package com.yandex.todolist

import TaskEditorScreen
import ToDoAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.todolist.presentation.ui.LoginScreen
import com.yandex.todolist.presentation.ui.TaskListScreen
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Устанавливаем контент для MainActivity с использованием Jetpack Compose
        setContent {
            ToDoAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("taskList") {
                    // Удаляем `LoginScreen` из стека, чтобы пользователь не мог вернуться на него
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("taskList") {
            val taskViewModel: TaskViewModel = koinViewModel()
            TaskListScreen(
                viewModel = taskViewModel,
                onAddTask = { navController.navigate("taskEditor") },
                onEditTask = { task -> navController.navigate("taskEditor?taskId=${task.id}") }
            )
        }

        composable("taskEditor?taskId={taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            val taskViewModel: TaskViewModel = koinViewModel()
            TaskEditorScreen(
                viewModel = taskViewModel,
                taskId = taskId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
