package com.yandex.todolist.data.network

import retrofit2.HttpException
import java.io.IOException

object NetworkErrorHandler {

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения с сервером"
            is HttpException -> when (e.code()) {
                400 -> "Неправильный запрос"
                401 -> "Неверная авторизация"
                404 -> "Данные не найдены"
                500 -> "Ошибка сервера"
                else -> "Неизвестная ошибка"
            }
            else -> "Произошла ошибка"
        }
    }
}
