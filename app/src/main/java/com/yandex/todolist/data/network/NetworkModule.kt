// data/network/NetworkModule.kt
package com.yandex.todolist.data.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://hive.mrdekk.ru/"

    // Функция для предоставления ApiService
    fun provideApiService(context: Context): ApiService {
        val tokenProvider = DefaultTokenProvider(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
