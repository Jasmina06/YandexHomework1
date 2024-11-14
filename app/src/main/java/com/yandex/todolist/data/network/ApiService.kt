// data/network/ApiService.kt
package com.yandex.todolist.data.network

import com.yandex.todolist.data.network.TaskRequest
import com.yandex.todolist.data.network.TaskResponse
import retrofit2.http.*

interface ApiService {
    @GET("list")
    suspend fun getTasks(): List<TaskResponse>

    @POST("list")
    suspend fun addTask(@Body task: TaskRequest): TaskResponse

    @PATCH("list")
    suspend fun updateTasks(@Header("X-Last-Known-Revision") revision: Int, @Body tasks: List<TaskRequest>): List<TaskResponse>

    @DELETE("list/{id}")
    suspend fun deleteTask(@Path("id") id: Int): TaskResponse
}
