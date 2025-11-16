package uvg.plats.fixerly.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.*
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.screens.components.ScreenWithBottomNav


@Composable
fun LaborScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    var currentRoute by remember { mutableStateOf("home") }

    FixerlyTheme {
        ScreenWithBottomNav(
            currentRoute = currentRoute,
            onNavigate = { route -> currentRoute = route },
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToHome = onNavigateToHome,
            // onNavigateToMessages = onNavigateToMessages
        ) {
            LaborContent()
        }
    }
}

@Composable
fun LaborContent() {
    val isDarkMode = isSystemInDarkTheme()

    val categorias = listOf(
        "Plomería",
        "Electricidad",
        "Electrodomésticos",
        "Cerrajería",
        "Jardinería y exteriores",
        "Limpieza y lavandería",
        "Pintura y acabados",
        "Otros..."
    )

    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var descripcionBreve by remember { mutableStateOf(TextFieldValue("")) }
    var descripcionDetallada by remember { mutableStateOf(TextFieldValue("")) }

    // ========================================
    // FONDO CON DEGRADADO (como light mode)
    // ========================================
    val backgroundBrush = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                DarkBackgroundTop,      // Azul muy oscuro arriba
                DarkBackgroundBottom    // Azul oscuro abajo
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(White, White)  // Blanco sólido en light mode
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)  // ← DEGRADADO aplicado
            .verticalScroll(rememberScrollState())
    ) {
        // ========================================
        // BANNER FIXERLY (sin cambios)
        // ========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 12.dp),  // ← Reducido de 16dp a 12dp
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(36.dp)  // ← Reducido de 40dp a 36dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fixerly.",
                    fontSize = 28.sp,  // ← Reducido de 32sp a 28sp
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }

        // ========================================
        // IMAGEN DE BIENVENIDA (más delgada)
        // ========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)  // ← REDUCIDO de 160dp a 100dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.herramientas),
                contentDescription = "Imagen de herramientas",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Manos a la\nobra!",
                    fontSize = 36.sp,  // ← Reducido de 42sp a 36sp
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp  // ← Reducido de 46sp a 40sp
                )
            }
        }

        // ========================================
        // CONTENIDO PRINCIPAL
        // ========================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)  // ← Reducido padding vertical
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ========================================
                // COLUMNA IZQUIERDA: Categorías
                // ========================================
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Selecciona la categoría\nde la problemática en la\nque te podemos ayudar:",
                        color = MaterialTheme.colorScheme.onBackground,  // ← Texto adaptable
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,  // ← Reducido de 15sp a 14sp
                        lineHeight = 17.sp  // ← Reducido de 18sp a 17sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))  // ← Reducido de 10dp a 8dp

                    categorias.forEach { categoria ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),  // ← Reducido de 3dp a 2dp
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (categoriaSeleccionada == categoria) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface  // ← Card con color correcto
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Text(
                                text = categoria,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { categoriaSeleccionada = categoria }
                                    .padding(8.dp),  // ← Reducido de 10dp a 8dp
                                color = if (categoriaSeleccionada == categoria) {
                                    White
                                } else {
                                    MaterialTheme.colorScheme.onSurface  // ← Texto adaptable
                                },
                                fontSize = 12.sp  // ← Reducido de 13sp a 12sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))  // ← Reducido de 20dp a 16dp

                // ========================================
                // COLUMNA DERECHA: Descripciones
                // ========================================
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Describe brevemente tu\nproblemática",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Recuerda: tienes un máximo\nde 10 palabras",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = descripcionBreve,
                        onValueChange = {
                            if (it.text.split(" ").filter { word -> word.isNotBlank() }.size <= 10) {
                                descripcionBreve = it
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                "Escribe aquí",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "¡Ahora sí! Cuéntanos\nmás a detalle como te\npodemos ayudar",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = descripcionDetallada,
                        onValueChange = { descripcionDetallada = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = {
                            Text(
                                "Escribe aquí",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))  // ← Reducido de 20dp a 16dp

            // ========================================
            // BOTÓN ENVIAR
            // ========================================
            Button(
                onClick = {
                    // Enviar solicitud a la base de datos
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),  // ← Reducido de 52dp a 50dp
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    "Enviar mi solicitud",
                    fontSize = 16.sp,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))  // ← Reducido de 16dp a 12dp
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaborScreenPreview() {
    LaborScreen()
}