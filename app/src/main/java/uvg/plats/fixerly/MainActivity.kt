package uvg.plats.fixerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import uvg.plats.fixerly.ui.navigation.NavigationGraph
import uvg.plats.fixerly.ui.theme.DarkStatusBar
import uvg.plats.fixerly.ui.theme.FixerlyBlue
import uvg.plats.fixerly.ui.theme.FixerlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // CAMBIO: Permite que el contenido se dibuje detrás de la status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FixerlyTheme {
                val systemUiController = rememberSystemUiController()
                val isDark = isSystemInDarkTheme()

                // CAMBIO: Configuración mejorada de status bar
                SideEffect {
                    val statusBarColor = if (isDark) DarkStatusBar else FixerlyBlue

                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = false  // Iconos siempre blancos
                    )

                    // NUEVO: Configurar también la navigation bar (opcional)
                    systemUiController.setNavigationBarColor(
                        color = statusBarColor,
                        darkIcons = false
                    )

                    // NUEVO: Asegurar que el color se aplique en toda la barra
                    window.statusBarColor = statusBarColor.toArgb()
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationGraph()
                }
            }
        }
    }
}