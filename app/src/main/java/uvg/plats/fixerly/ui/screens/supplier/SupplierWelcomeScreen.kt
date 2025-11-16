package uvg.plats.fixerly.ui.screens.supplier

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.*
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.screens.components.ScreenWithBottomNav

data class Cliente(
    val nombre: String,
    val zona: String,
    val problema: String,
    val descripcion: String
)

@Composable
fun SupplierWelcomeScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    // onNavigateToMessages: () -> Unit = {}
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
            SupplierWelcomeScreenContent()
        }
    }
}

@Composable
fun SupplierWelcomeScreenContent() {
    val isDarkMode = isSystemInDarkTheme()

    val categorias = listOf(
        "Plomería",
        "Limpieza y lavandería",
        "Electrodomésticos",
        "Jardín y exteriores",
        "Pintura y acabados",
        "Electricidad",
        "Cerrajería",
        "Otros",
        "Todos"
    )

    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val clientes = listOf(
        Cliente(
            nombre = "Juan Pérez",
            zona = "Zona 10, Ciudad de Guatemala",
            problema = "Falla de electrodomésticos",
            descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        ),
        Cliente(
            nombre = "María González",
            zona = "Zona 15, Ciudad de Guatemala",
            problema = "Instalación de electrodomésticos",
            descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        ),
        Cliente(
            nombre = "Carlos Mendoza",
            zona = "Zona 1, Ciudad de Guatemala",
            problema = "Reparación de electrodomésticos",
            descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        ),
        Cliente(
            nombre = "Ana Martínez",
            zona = "Zona 9, Ciudad de Guatemala",
            problema = "Mantenimiento de electrodomésticos",
            descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        ),
        Cliente(
            nombre = "Roberto Castillo",
            zona = "Zona 4, Ciudad de Guatemala",
            problema = "Falla de refrigerador",
            descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        )
    )


    val backgroundBrush = if (isDarkMode) {
        Brush.verticalGradient(
            colors = listOf(
                DarkBackgroundTop,
                DarkBackgroundBottom
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(White, White)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fixerly.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.herramientas),
                contentDescription = "Imagen de bienvenida",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Bienvenido!",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Filtrar por categoría:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { categoriaSeleccionada = categoria },
                        label = {
                            Text(
                                text = categoria,
                                fontSize = 13.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            thickness = 1.dp
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(clientes) { cliente ->
                ClientCard(cliente = cliente)
            }
        }
    }
}

@Composable
fun ClientCard(cliente: Cliente) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = cliente.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { /* Lógica para enviar información */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Enviar información",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ubicación: ${cliente.zona}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Problema: ${cliente.problema}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descripción del problema: ${cliente.descripcion}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierWelcomeScreenPreview() {
    SupplierWelcomeScreen()
}