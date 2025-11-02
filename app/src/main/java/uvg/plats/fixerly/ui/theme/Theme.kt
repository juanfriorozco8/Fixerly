package uvg.plats.fixerly.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White


private val LightColorScheme = lightColorScheme(
    primary = FixerlyBlue,
    onPrimary = White,
    primaryContainer = FixerlyBlueDark,
    onPrimaryContainer = White,

    secondary = FixerlyGreen,
    onSecondary = White,

    background = FixerlyBlue,
    onBackground = White,

    surface = TextFieldBackground,
    onSurface = TextFieldText,

    surfaceVariant = TextFieldBackground,
    onSurfaceVariant = TextFieldPlaceholder
)

private val DarkColorScheme = darkColorScheme(
    primary = FixerlyBlue,
    onPrimary = White,
    primaryContainer = FixerlyBlueDark,
    onPrimaryContainer = White,

    secondary = FixerlyGreen,
    onSecondary = White,

    background = FixerlyBlueDark,
    onBackground = White,

    surface = TextFieldBackground,
    onSurface = TextFieldText,

    surfaceVariant = TextFieldBackground,
    onSurfaceVariant = TextFieldPlaceholder
)

@Composable
fun FixerlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}