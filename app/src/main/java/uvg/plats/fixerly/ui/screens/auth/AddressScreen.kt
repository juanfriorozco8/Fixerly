package uvg.plats.fixerly.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.viewmodel.AuthViewModel
import uvg.plats.fixerly.ui.viewmodel.AuthState

@Composable
fun AddressScreen(
    onComplete: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var departamento by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var zona by remember { mutableStateOf("") }
    var indicaciones by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                // ✅ Ya se registró el cliente y Firebase lo dejó logeado
                onComplete()               // navega a LaborDestination
                viewModel.resetAuthState() // limpia el estado
            }

            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetAuthState()
            }

            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp)
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_icon),
                        contentDescription = stringResource(R.string.onboarding_logo_description),
                        modifier = Modifier.size(70.dp),
                        alignment = Alignment.Center
                    )

                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(90.dp))
                Text(
                    text = stringResource(R.string.address_ready_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.address_add_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    modifier = Modifier.width(200.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 2.dp
                )

                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        AddressField(
                            label = stringResource(R.string.address_department),
                            value = departamento,
                            onValueChange = { departamento = it }
                        )

                        AddressField(
                            label = stringResource(R.string.address_zone),
                            value = zona,
                            onValueChange = { zona = it }
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        AddressField(
                            label = stringResource(R.string.address_street),
                            value = direccion,
                            onValueChange = { direccion = it }
                        )

                        AddressField(
                            label = stringResource(R.string.address_directions),
                            value = indicaciones,
                            onValueChange = { indicaciones = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                    onClick = {
                        viewModel.registerClient(
                            department = departamento,
                            address = direccion,
                            zone = zona,
                            directions = indicaciones
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    enabled = authState !is AuthState.Loading &&
                            departamento.isNotBlank() &&
                            direccion.isNotBlank() &&
                            zona.isNotBlank()
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    } else {
                        Text(
                            stringResource(R.string.address_continue),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 6.dp),
            fontWeight = FontWeight.Medium
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.address_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 13.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddressScreenPreview() {
    FixerlyTheme {
        AddressScreen()
    }
}
