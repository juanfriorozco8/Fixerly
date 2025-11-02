package uvg.plats.fixerly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.ui.theme.White
import uvg.plats.fixerly.R

@Composable
fun UserProfileScreen(
    onNavigateToProfile: () -> Unit = {},   // ← NUEVO: Navegar a perfil (esta misma pantalla)
    onNavigateToHome: () -> Unit = {},      // ← NUEVO: Navegar a home
    onNavigateToMessages: () -> Unit = {}   // ← NUEVO: Navegar a mensajes
) {
    var currentRoute by remember { mutableStateOf("profile") }

    FixerlyTheme {
        ScreenWithBottomNav(
            currentRoute = currentRoute,
            onNavigate = { route -> currentRoute = route },
            onNavigateToProfile = onNavigateToProfile,      // ← CAMBIO: Pasar callbacks
            onNavigateToHome = onNavigateToHome,
            onNavigateToMessages = onNavigateToMessages
        ) {
            UserProfileContent()
        }
    }
}

@Composable
fun UserProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fixerly.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(180.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Nombre:",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Nombre del Usuario",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Apellidos:",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Apellidos del Usuario",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Número de celular:",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "1111 1111",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Correo electrónico:",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Usuario@gmail.com",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Cambiar Contraseña",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "Usuario@gmail.com",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}