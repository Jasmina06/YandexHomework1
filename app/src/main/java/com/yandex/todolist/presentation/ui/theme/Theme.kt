// Theme.kt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.yandex.todolist.presentation.ui.theme.Typography
import androidx.compose.ui.unit.dp

// Настраиваем тёмную цветовую палитру
private val DarkColorPalette = darkColorScheme(
    primary = DarkPrimaryLabel,           // Основной цвет текста
    secondary = DarkGreen,                // Второстепенный цвет, например, для иконок или выделений
    tertiary = DarkBlue,                  // Дополнительный цвет, если нужен
    background = DarkPrimaryBackground,   // Цвет фона приложения
    surface = DarkSecondaryBackground,    // Цвет поверхности (например, карточек или контейнеров)
    onPrimary = DarkWhite,                // Цвет текста на первичном фоне
    onSecondary = DarkWhite,              // Цвет текста на вторичном фоне
    onBackground = DarkPrimaryLabel,      // Цвет текста на фоне
    onSurface = DarkSecondaryLabel        // Цвет текста на элементах интерфейса
)

// Определяем формы для элементов интерфейса
private val AppShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorPalette else lightColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
