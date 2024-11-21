
// data/workers/PeriodicSyncWorker.kt
package com.yandex.todolist.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.todolist.domain.repository.TaskRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.coroutines.flow.collect

class PeriodicSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val taskRepository: TaskRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            taskRepository.getTasks().collect {

            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
