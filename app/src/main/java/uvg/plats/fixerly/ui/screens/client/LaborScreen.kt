package uvg.plats.fixerly.ui.screens.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.viewmodel.ServiceViewModel
import uvg.plats.fixerly.ui.viewmodel.OperationState
import uvg.plats.fixerly.data.model.Address

@Composable
fun LaborScreen(
    viewModel: ServiceViewModel = viewModel()
) {
    val White = Color(0xFFFFFFFF)
    var descripcionDetallada by remember { mutableStateOf("") }
    var selectedServiceType by remember { mutableStateOf("") }

    val operationState by viewModel.operationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(operationState) {
        when (val state = operationState) {
            is OperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                descripcionDetallada = ""
                selectedServiceType = ""
            }
            is OperationState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.message.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.labor_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.labor_subtitle),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.labor_filter_label),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedServiceType == "Plomería",
                            onClick = { selectedServiceType = "Plomería" },
                            label = { Text(stringResource(R.string.labor_filter_plumbing)) }
                        )
                        FilterChip(
                            selected = selectedServiceType == "Electricidad",
                            onClick = { selectedServiceType = "Electricidad" },
                            label = { Text(stringResource(R.string.labor_filter_electricity)) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedServiceType == "Carpintería",
                            onClick = { selectedServiceType = "Carpintería" },
                            label = { Text(stringResource(R.string.labor_filter_carpentry)) }
                        )
                        FilterChip(
                            selected = selectedServiceType == "Pintura",
                            onClick = { selectedServiceType = "Pintura" },
                            label = { Text(stringResource(R.string.labor_filter_painting)) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedServiceType == "Jardinería",
                            onClick = { selectedServiceType = "Jardinería" },
                            label = { Text(stringResource(R.string.labor_filter_gardening)) }
                        )
                        FilterChip(
                            selected = selectedServiceType == "Limpieza",
                            onClick = { selectedServiceType = "Limpieza" },
                            label = { Text(stringResource(R.string.labor_filter_cleaning)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.labor_description_label),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = descripcionDetallada,
                        onValueChange = { descripcionDetallada = it },
                        placeholder = { Text(stringResource(R.string.labor_description_placeholder)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.createServiceRequest(
                        clientId = "temp_client_id",
                        clientName = "Cliente Temporal",
                        serviceType = selectedServiceType,
                        description = descripcionDetallada,
                        address = Address("", "", "", "")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                enabled = operationState !is OperationState.Loading
            ) {
                if (operationState is OperationState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.labor_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaborScreenPreview() {
    FixerlyTheme {
        LaborScreen()
    }
}