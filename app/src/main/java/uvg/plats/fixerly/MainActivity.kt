package uvg.plats.fixerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import uvg.plats.fixerly.ui.navigation.NavigationGraph
import uvg.plats.fixerly.ui.theme.DarkStatusBar
import uvg.plats.fixerly.ui.theme.FixerlyBlue
import uvg.plats.fixerly.ui.theme.FixerlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FixerlyTheme {
                val systemUiController = rememberSystemUiController()
                val isDark = isSystemInDarkTheme()

                SideEffect {
                    val statusColor = if (isDark) DarkStatusBar else FixerlyBlue

                    systemUiController.setStatusBarColor(
                        color = statusColor,
                        darkIcons = false
                    )

                    systemUiController.setNavigationBarColor(
                        color = statusColor,
                        darkIcons = false
                    )
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationGraph()
                }
            }
        }
    }
}