package uvg.plats.fixerly.ui.screens.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.theme.White

sealed class BottomNavItem(
    val route: String,
    val iconRes: Int,
    val label: String
) {
    object Profile : BottomNavItem("profile", R.drawable.ic_profile, "Perfil")
    object Home : BottomNavItem("home", R.drawable.ic_home, "Inicio")
}

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Home,
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(90.dp)  // ← CAMBIO: De 70dp a 90dp (más grueso)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(32.dp),  // ← CAMBIO: De 28dp a 32dp
                        tint = if (currentRoute == item.route) White else White.copy(alpha = 0.6f)  // ← NUEVO: Tint explícito
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = White,
                    unselectedIconColor = White.copy(alpha = 0.6f),
                    selectedTextColor = White,
                    unselectedTextColor = White.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier  // ← NUEVO: Alineación vertical
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun ScreenWithBottomNav(
    currentRoute: String = "home",
    onNavigate: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    when (route) {
                        "profile" -> onNavigateToProfile()
                        "home" -> onNavigateToHome()
                    }
                    onNavigate(route)
                }
            )
        }
    ) { paddingValues ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            content()
        }
    }
}