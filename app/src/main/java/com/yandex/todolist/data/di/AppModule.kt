package com.yandex.todolist.di

import com.yandex.todolist.data.datasource.TaskLocalDataSource
import com.yandex.todolist.data.datasource.TaskRemoteDataSource
import com.yandex.todolist.data.local.TaskDatabase
import com.yandex.todolist.data.network.NetworkModule
import com.yandex.todolist.data.repository.TaskRepositoryImpl
import com.yandex.todolist.domain.repository.TaskRepository
import com.yandex.todolist.domain.usecase.*
import com.yandex.todolist.presentation.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Источники данных
    single { TaskLocalDataSource(get()) }
    single { TaskRemoteDataSource(get()) }

    // DAO и база данных
    single { TaskDatabase.getDatabase(get()).taskDao() }

    // Сетевой модуль
    single { NetworkModule.provideApiService(get()) }

    // Репозиторий
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }

    // Юз-кейсы
    factory { GetTasksUseCase(get()) }
    factory { AddTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }

    // ViewModel
    viewModel { TaskViewModel(get(), get(), get(), get()) }
}
