package com.yandex.todolist.data.network

import android.content.Context
import android.content.SharedPreferences

class DefaultTokenProvider(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return preferences.getString("auth_token", null)
    }

    fun saveToken(token: String) {
        preferences.edit().putString("auth_token", token).apply()
    }
}
