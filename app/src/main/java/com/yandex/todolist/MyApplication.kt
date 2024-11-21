// MyApplication.kt
package com.yandex.todolist

import android.app.Application
import com.yandex.todolist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule) // Убедитесь, что appModule правильно добавлен
        }
    }
}
