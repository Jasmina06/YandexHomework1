// AppModule.kt
package com.yandex.todolist.di

import com.yandex.todolist.data.local.TaskDatabase
import com.yandex.todolist.data.network.NetworkModule
import com.yandex.todolist.data.repository.TaskRepositoryImpl
import com.yandex.todolist.domain.repository.TaskRepository
import com.yandex.todolist.domain.usecase.*
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Регистрация ApiService с помощью NetworkModule
    single { NetworkModule.provideApiService(get()) }

    // Единственный экземпляр базы данных и DAO
    single { TaskDatabase.getDatabase(get()).taskDao() }

    // Репозиторий для работы с задачами
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }

    // Юз-кейсы для работы с задачами
    factory { GetTasksUseCase(get()) }
    factory { AddTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }

    // ViewModel для работы с задачами
    viewModel { TaskViewModel(get(), get(), get(), get()) }
}
