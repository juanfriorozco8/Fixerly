package uvg.plats.fixerly.ui.screens.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.theme.White
import uvg.plats.fixerly.ui.theme.DarkBackgroundBottom

@Composable
fun ClientBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = DarkBackgroundBottom,
        contentColor = White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(90.dp)
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentRoute == "profile") {
                        White
                    } else {
                        White.copy(alpha = 0.6f)
                    }
                )
            },
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                unselectedIconColor = White.copy(alpha = 0.6f),
                selectedTextColor = White,
                unselectedTextColor = White.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        NavigationBarItem(
            icon = {
                Text(
                    text = "+",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Light,
                    color = if (currentRoute == "new_request") {
                        White
                    } else {
                        White.copy(alpha = 0.8f)
                    }
                )
            },
            selected = currentRoute == "new_request",
            onClick = { onNavigate("new_request") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                unselectedIconColor = White.copy(alpha = 0.6f),
                selectedTextColor = White,
                unselectedTextColor = White.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Solicitudes",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentRoute == "requests") {
                        White
                    } else {
                        White.copy(alpha = 0.6f)
                    }
                )
            },
            selected = currentRoute == "requests",
            onClick = { onNavigate("requests") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = White,
                unselectedIconColor = White.copy(alpha = 0.6f),
                selectedTextColor = White,
                unselectedTextColor = White.copy(alpha = 0.6f),
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun ClientScreenWithBottomNav(
    currentRoute: String = "requests",
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRequests: () -> Unit = {},
    onNavigateToNewRequest: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            ClientBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    when (route) {
                        "profile" -> onNavigateToProfile()
                        "requests" -> onNavigateToRequests()
                        "new_request" -> onNavigateToNewRequest()
                    }
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