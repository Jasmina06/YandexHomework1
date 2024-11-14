// presentation/ui/LoginScreen.kt
package com.yandex.todolist.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.todolist.R
import com.yandex.todolist.data.network.DefaultTokenProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val tokenProvider = DefaultTokenProvider(context)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Получаем строку заранее, чтобы избежать ошибки
    val loginSuccessfulMessage = stringResource(R.string.login_successful)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Логотип или иконка приложения
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),  // замените на логотип вашего приложения
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 24.dp)
        )

        // Заголовок экрана
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Поле ввода для имени пользователя
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Поле ввода для пароля
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Кнопка входа
        Button(
            onClick = {
                coroutineScope.launch {
                    val token = "Elanor"  // Здесь используется ваш фиксированный токен
                    tokenProvider.saveToken(token)
                    Toast.makeText(context, loginSuccessfulMessage, Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Текст с подписью или ссылкой
        Text(
            text = "Забыли пароль?",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { /* Handle forgot password */ }
        )
    }
}
