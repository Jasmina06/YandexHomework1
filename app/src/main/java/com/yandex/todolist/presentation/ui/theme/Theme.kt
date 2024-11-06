// Theme.kt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.yandex.todolist.presentation.ui.theme.Typography

// Определяем цветовые схемы для темной и светлой тем
private val DarkColorPalette = darkColorScheme(
    primary = DarkPrimaryLabel,
    secondary = DarkGreen,
    tertiary = DarkBlue,
    background = DarkPrimaryBackground,
    surface = DarkElevatedBackground,
    onPrimary = DarkWhite,
    onSecondary = DarkWhite,
    onBackground = DarkPrimaryLabel,
    onSurface = DarkSecondaryLabel
)

private val LightColorPalette = lightColorScheme(
    primary = LightPrimaryLabel,
    secondary = LightGreen,
    tertiary = LightBlue,
    background = LightPrimaryBackground,
    surface = LightElevatedBackground,
    onPrimary = LightPrimaryLabel,
    onSecondary = LightPrimaryLabel,
    onBackground = LightPrimaryLabel,
    onSurface = LightSecondaryLabel
)

// Определяем пользовательские формы, если необходимо
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
    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Убедитесь, что com.yandex.todolist.presentation.ui.theme.getTypography определен корректно
        shapes = AppShapes,
        content = content
    )
}
