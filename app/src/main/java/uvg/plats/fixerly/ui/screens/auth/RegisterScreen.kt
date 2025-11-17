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
import android.util.Patterns

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToAccountType: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // validaciones en tiempo real
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // validar email cuando cambia
    LaunchedEffect(email) {
        emailError = when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo electrónico inválido"
            else -> null
        }
    }

    // validar contraseña
    LaunchedEffect(password) {
        passwordError = when {
            password.isEmpty() -> null
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onNavigateToAccountType()
                viewModel.resetAuthState()
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_icon),
                        contentDescription = stringResource(R.string.onboarding_logo_description),
                        modifier = Modifier.size(70.dp)
                    )

                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.register_title),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                FormField(
                    label = stringResource(R.string.register_name),
                    value = name,
                    onValueChange = { name = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                FormField(
                    label = stringResource(R.string.register_lastname),
                    value = lastName,
                    onValueChange = { lastName = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                FormField(
                    label = stringResource(R.string.register_phone),
                    value = phone,
                    onValueChange = { phone = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email con validación
                FormFieldWithError(
                    label = stringResource(R.string.register_email),
                    value = email,
                    onValueChange = { email = it },
                    errorMessage = emailError
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password con validación
                FormFieldWithError(
                    label = stringResource(R.string.register_password),
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true,
                    errorMessage = passwordError
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        viewModel.saveTempRegistrationData(
                            name = name,
                            lastName = lastName,
                            email = email,
                            password = password,
                            phone = phone
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    enabled = authState !is AuthState.Loading &&
                            emailError == null &&
                            passwordError == null &&
                            name.isNotBlank() &&
                            lastName.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank()
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.login_next_button),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.register_already_have_account),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = stringResource(R.string.register_login_link),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
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
                    text = stringResource(R.string.login_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
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

@Composable
fun FormFieldWithError(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
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
                    text = stringResource(R.string.login_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            isError = errorMessage != null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    FixerlyTheme {
        RegisterScreen()
    }
}