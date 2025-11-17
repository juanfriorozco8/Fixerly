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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uvg.plats.fixerly.ui.theme.*
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.screens.components.ScreenWithBottomNav
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Cliente(
    val nombre: String,
    val zona: String,
    val problema: String,
    val descripcion: String,
    val categoria: String // para el filtro
)

@Composable
fun SupplierWelcomeScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    var currentRoute by remember { mutableStateOf("home") }

    FixerlyTheme {
        ScreenWithBottomNav(
            currentRoute = currentRoute,
            onNavigate = { route -> currentRoute = route },
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToHome = onNavigateToHome,
        ) {
            SupplierWelcomeScreenContent()
        }
    }
}

@Composable
fun SupplierWelcomeScreenContent() {
    val isDarkMode = isSystemInDarkTheme()

    val categorias = listOf(
        stringResource(R.string.labor_filter_plumbing),
        stringResource(R.string.labor_filter_cleaning),
        stringResource(R.string.labor_filter_appliances),
        stringResource(R.string.labor_filter_gardening),
        stringResource(R.string.labor_filter_painting),
        stringResource(R.string.labor_filter_electricity),
        stringResource(R.string.labor_filter_locksmith),
        stringResource(R.string.labor_filter_other),
        stringResource(R.string.supplier_welcome_filter_all)
    )

    var categoriaSeleccionada by remember { mutableStateOf(categorias.last()) }

    // Lista completa de clientes con categorías
    val clientesCompletos = remember {
        listOf(
            Cliente(
                nombre = "Juan Pérez",
                zona = "Zona 10, Ciudad de Guatemala",
                problema = "Falla de electrodomésticos",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Electrodomésticos"
            ),
            Cliente(
                nombre = "María González",
                zona = "Zona 15, Ciudad de Guatemala",
                problema = "Instalación de electrodomésticos",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Electrodomésticos"
            ),
            Cliente(
                nombre = "Carlos Mendoza",
                zona = "Zona 1, Ciudad de Guatemala",
                problema = "Reparación de tubería",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Plomería"
            ),
            Cliente(
                nombre = "Ana Martínez",
                zona = "Zona 9, Ciudad de Guatemala",
                problema = "Mantenimiento de jardín",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Jardinería y exteriores"
            ),
            Cliente(
                nombre = "Roberto Castillo",
                zona = "Zona 4, Ciudad de Guatemala",
                problema = "Falla eléctrica",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Electricidad"
            ),
            Cliente(
                nombre = "Sofía Hernández",
                zona = "Zona 7, Ciudad de Guatemala",
                problema = "Pintura de casa",
                descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                categoria = "Pintura y acabados"
            )
        )
    }

    // filtrar según categoría
    val clientesFiltrados = remember(categoriaSeleccionada) {
        if (categoriaSeleccionada == categorias.last()) { // "Todos"
            clientesCompletos
        } else {
            clientesCompletos.filter { it.categoria == categoriaSeleccionada }
        }
    }

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
                    contentDescription = stringResource(R.string.onboarding_logo_description),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.app_name),
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
                contentDescription = stringResource(R.string.supplier_welcome_image_description),
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
                    text = stringResource(R.string.supplier_welcome_greeting),
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
                text = stringResource(R.string.supplier_welcome_filter_title),
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

        // mostrar mensaje si no hay resultados
        if (clientesFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay solicitudes en esta categoría",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(clientesFiltrados) { cliente ->
                    ClientCard(cliente = cliente)
                }
            }
        }
    }
}

@Composable
fun ClientCard(cliente: Cliente) {
    var buttonState by remember { mutableStateOf(ButtonState.IDLE) }

    LaunchedEffect(buttonState) {
        if (buttonState == ButtonState.SENT) {
            delay(2000) // Mantener verde por 2 segundos
            buttonState = ButtonState.IDLE
        }
    }

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
                            contentDescription = stringResource(R.string.supplier_welcome_profile_description),
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
                    onClick = {
                        buttonState = ButtonState.SENDING
                        // Simular envío
                        kotlinx.coroutines.GlobalScope.launch {
                            delay(500)
                            buttonState = ButtonState.SENT
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (buttonState) {
                            ButtonState.IDLE -> MaterialTheme.colorScheme.primary
                            ButtonState.SENDING -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            ButtonState.SENT -> Color(0xFF52B788) // Verde
                        }
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    enabled = buttonState != ButtonState.SENDING
                ) {
                    when (buttonState) {
                        ButtonState.IDLE -> {
                            Text(
                                text = stringResource(R.string.supplier_welcome_send_info),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        ButtonState.SENDING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        }
                        ButtonState.SENT -> {
                            Text(
                                text = "✓ Enviado",
                                fontSize = 12.sp,
                                color = White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.supplier_welcome_location_label) + " ${cliente.zona}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.supplier_welcome_problem_label) + " ${cliente.problema}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.supplier_welcome_description_label) + " ${cliente.descripcion}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

enum class ButtonState {
    IDLE,
    SENDING,
    SENT
}

@Preview(showBackground = true)
@Composable
fun SupplierWelcomeScreenPreview() {
    SupplierWelcomeScreen()
}