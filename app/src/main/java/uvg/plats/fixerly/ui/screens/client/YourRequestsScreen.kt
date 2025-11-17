package uvg.plats.fixerly.ui.screens.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.*
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.screens.components.ClientScreenWithBottomNav
import uvg.plats.fixerly.ui.viewmodel.ServiceViewModel

@Composable
fun YourRequestsScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewRequest: () -> Unit = {},
    clientId: String = "",
    viewModel: ServiceViewModel = viewModel()
) {
    // Cambié a Unit para que se recargue cada vez que se navega a esta pantalla
    LaunchedEffect(Unit) {
        if (clientId.isNotEmpty()) {
            viewModel.loadClientRequests(clientId)
        }
    }

    ClientScreenWithBottomNav(
        currentRoute = "requests",
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToRequests = {},
        onNavigateToNewRequest = onNavigateToNewRequest
    ) {
        YourRequestsContent(
            viewModel = viewModel,
            clientId = clientId
        )
    }
}

@Composable
fun YourRequestsContent(
    viewModel: ServiceViewModel,
    clientId: String = ""
) {
    val isDarkMode = isSystemInDarkTheme()
    val clientRequests by viewModel.clientRequests.collectAsState()

    var solicitudExpandida by remember { mutableStateOf<String?>(null) }

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
                contentDescription = stringResource(R.string.your_requests_image_description),
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
                    text = stringResource(R.string.your_requests_title),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    lineHeight = 44.sp
                )
            }
        }

        when {
            clientRequests.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            clientRequests.hasError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = clientRequests.errorMessage ?: "Error al cargar solicitudes",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (clientId.isNotEmpty()) {
                                viewModel.loadClientRequests(clientId)
                            }
                        }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            clientRequests.data.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes solicitudes aún",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
            else -> {
                val requests = clientRequests.data ?: emptyList()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(requests) { request ->
                        SolicitudCard(
                            request = request,
                            isExpanded = solicitudExpandida == request.requestId,
                            onExpandClick = {
                                solicitudExpandida = if (solicitudExpandida == request.requestId) {
                                    null
                                } else {
                                    request.requestId
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudCard(
    request: uvg.plats.fixerly.data.model.ServiceRequest,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    // Extraer la descripción breve (primera parte antes del " - ")
    val briefDescription = request.description.split(" - ").firstOrNull() ?: request.description

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Aquí va el nombre del problema que escribió el usuario
                Text(
                    text = briefDescription,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(R.string.your_requests_view_full),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onExpandClick() },
                    lineHeight = 14.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = request.serviceType,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mostrar descripción detallada solo cuando está expandido
                if (isExpanded) {
                    val detailedDescription = request.description.split(" - ").getOrNull(1)
                    if (!detailedDescription.isNullOrBlank()) {
                        Text(
                            text = "Descripción:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = detailedDescription,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Text(
                    text = stringResource(R.string.your_requests_responses_label),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (request.responses.isEmpty()) {
                    Text(
                        text = "Sin respuestas aún",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    request.responses.forEachIndexed { index, respuesta ->
                        RespuestaProveedorItem(
                            respuesta = respuesta,
                            isExpanded = isExpanded
                        )

                        if (index < request.responses.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RespuestaProveedorItem(
    respuesta: uvg.plats.fixerly.data.model.ProviderResponse,
    isExpanded: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = stringResource(R.string.your_requests_profile_description),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = respuesta.providerName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${stringResource(R.string.your_requests_contact_label)} ${respuesta.providerEmail} / ${respuesta.providerPhone}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${stringResource(R.string.your_requests_skills_label)} ${respuesta.skills.joinToString(", ")}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
            )

            if (respuesta.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.your_requests_details_label)} ${respuesta.message}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YourRequestsScreenPreview() {
    YourRequestsScreen()
}