package uvg.plats.fixerly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.ui.theme.White
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.screens.components.ScreenWithBottomNav

@Composable
fun SupplierProfileScreen(
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
            SupplierProfileContent()
        }
    }
}

@Composable
fun SupplierProfileContent() {
    val habilidades = listOf(
        "Plomería",
        "Electricidad",
        "Electrodomésticos",
        "Cerrajería",
        "Jardinería y exteriores",
        "Limpieza y lavandería",
        "Pintura y acabados",
        "Otros..."
    )

    val habilidadesSeleccionadas = remember { mutableStateMapOf<String, Boolean>().apply {
        put("Plomería", true)
        put("Electricidad", true)
        put("Electrodomésticos", true)
    } }

    var detalles by remember { mutableStateOf(TextFieldValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState()),
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

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Nombre:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Nombre del Usuario",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Apellidos:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Apellidos del Usuario",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Número de celular:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1111 1111",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Correo electrónico:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Usuario@gmail.com",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cambiar Contraseña",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Usuario@gmail.com",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Detalles:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detalles.text,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Selecciona tus\nhabilidades:",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                habilidades.forEach { habilidad ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Checkbox(
                            checked = habilidadesSeleccionadas[habilidad] == true,
                            onCheckedChange = { habilidadesSeleccionadas[habilidad] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.primary,
                                checkmarkColor = White
                            )
                        )
                        Text(
                            text = habilidad,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierProfileScreenPreview() {
    SupplierProfileScreen()
}