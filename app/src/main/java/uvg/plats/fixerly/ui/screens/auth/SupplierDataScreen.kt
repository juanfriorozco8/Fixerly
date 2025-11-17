package uvg.plats.fixerly.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.viewmodel.AuthViewModel
import uvg.plats.fixerly.ui.viewmodel.AuthState
import kotlinx.coroutines.delay

@Composable
fun SupplierDataScreen(
    accountType: String = "Proveedor",
    onNavigateBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var contactoEmail by remember { mutableStateOf(false) }
    var contactoTelefono by remember { mutableStateOf(false) }

    val habilidades = listOf(
        stringResource(R.string.labor_filter_plumbing),
        stringResource(R.string.labor_filter_electricity),
        stringResource(R.string.labor_filter_appliances),
        stringResource(R.string.labor_filter_locksmith),
        stringResource(R.string.labor_filter_gardening),
        stringResource(R.string.labor_filter_cleaning),
        stringResource(R.string.labor_filter_painting),
        stringResource(R.string.labor_filter_other)
    )

    val habilidadesSeleccionadas = remember { mutableStateMapOf<String, Boolean>() }
    habilidades.forEach { habilidad ->
        if (habilidadesSeleccionadas[habilidad] == null)
            habilidadesSeleccionadas[habilidad] = false
    }

    var detalles by remember { mutableStateOf(TextFieldValue("")) }

    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (val state = authState) {
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
        FixerlyTheme {
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(vertical = 40.dp)
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_icon),
                            contentDescription = stringResource(R.string.onboarding_logo_description),
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = stringResource(R.string.app_name),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.address_ready_title),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = stringResource(R.string.supplier_data_subtitle),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.supplier_data_info),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.supplier_data_select_skills),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            habilidades.forEach { habilidad ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(vertical = 0.dp)
                                        .height(32.dp)
                                ) {
                                    Checkbox(
                                        checked = habilidadesSeleccionadas[habilidad] == true,
                                        onCheckedChange = { habilidadesSeleccionadas[habilidad] = it },
                                        colors = CheckboxDefaults.colors(
                                            checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = habilidad,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.supplier_data_contact_method),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 0.dp)
                                    .height(32.dp)
                            ) {
                                Checkbox(
                                    checked = contactoEmail,
                                    onCheckedChange = { contactoEmail = it },
                                    colors = CheckboxDefaults.colors(
                                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.supplier_data_contact_email),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 0.dp)
                                    .height(32.dp)
                            ) {
                                Checkbox(
                                    checked = contactoTelefono,
                                    onCheckedChange = { contactoTelefono = it },
                                    colors = CheckboxDefaults.colors(
                                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.supplier_data_contact_phone),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = stringResource(R.string.supplier_data_details_optional),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = detalles,
                                onValueChange = { detalles = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                shape = RoundedCornerShape(16.dp),
                                placeholder = {
                                    Text(
                                        stringResource(R.string.supplier_data_details_placeholder),
                                        fontSize = 12.sp
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            val contactPrefs = mutableListOf<String>()
                            if (contactoEmail) contactPrefs.add("email")
                            if (contactoTelefono) contactPrefs.add("telefono")

                            val skillsList = habilidadesSeleccionadas
                                .filter { it.value }
                                .map { it.key }

                            viewModel.registerProvider(
                                contactPreferences = contactPrefs,
                                about = detalles.text,
                                skills = skillsList
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        enabled = authState !is AuthState.Loading
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        } else {
                            Text(
                                stringResource(R.string.supplier_data_finish),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.supplier_data_forgot_something),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = stringResource(R.string.supplier_data_go_back),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onNavigateBack() }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupplierDataScreenPreview() {
    SupplierDataScreen()
}