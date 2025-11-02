package uvg.plats.fixerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import uvg.plats.fixerly.ui.navigation.NavigationGraph
import uvg.plats.fixerly.ui.theme.FixerlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FixerlyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationGraph()
                }
            }
        }
    }
}