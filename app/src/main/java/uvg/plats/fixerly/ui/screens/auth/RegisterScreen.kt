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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.plats.fixerly.ui.theme.FixerlyTheme
import uvg.plats.fixerly.R
import uvg.plats.fixerly.ui.viewmodel.AuthViewModel
import uvg.plats.fixerly.ui.viewmodel.AuthState

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToAccountType: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                onNavigateToAccountType()
            }
            is AuthState.Error -> {
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
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_icon),
                            contentDescription = stringResource(R.string.logo_description),
                            modifier = Modifier.size(70.dp)
                        )

                        Text(
                            text = stringResource(R.string.app_name_display),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.register_title),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.register_subtitle),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    FormField(
                        label = stringResource(R.string.register_name_label),
                        value = nombre,
                        onValueChange = { nombre = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FormField(
                        label = stringResource(R.string.register_phone_label),
                        value = telefono,
                        onValueChange = { telefono = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FormField(
                        label = stringResource(R.string.register_email_label),
                        value = email,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FormField(
                        label = stringResource(R.string.register_password_label),
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            viewModel.registerClient(
                                name = nombre,
                                lastName = "",
                                email = email,
                                password = password,
                                phone = telefono,
                                department = "",
                                address = "",
                                zone = "",
                                directions = ""
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        enabled = authState !is AuthState.Loading
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.register_button),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.register_have_account),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.register_login),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    FixerlyTheme {
        RegisterScreen()
    }
}