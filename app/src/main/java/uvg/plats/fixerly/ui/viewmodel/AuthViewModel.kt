package uvg.plats.fixerly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.data.repository.AuthRepository
import uvg.plats.fixerly.utils.ErrorMessages
import uvg.plats.fixerly.utils.Validators

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val firebaseUser = repository.getCurrentUser()
            if (firebaseUser != null) {
                loadUserData(firebaseUser.uid)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val emailValidation = Validators.validateEmail(email)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(emailValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val passwordValidation = Validators.validatePassword(password)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(passwordValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            repository.login(email, password).fold(
                onSuccess = { userId ->
                    loadUserData(userId)
                    _authState.value = AuthState.Success(userId)
                    _message.emit("Bienvenido!")
                },
                onFailure = { error ->
                    val errorMsg = when {
                        error.message?.contains("password") == true -> "Contraseña incorrecta"
                        error.message?.contains("user") == true -> "Usuario no encontrado"
                        error.message?.contains("network") == true -> ErrorMessages.NETWORK_ERROR
                        else -> ErrorMessages.AUTH_FAILED
                    }
                    _authState.value = AuthState.Error(errorMsg)
                    _message.emit(errorMsg)
                }
            )
        }
    }

    fun registerClient(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        department: String,
        address: String,
        zone: String,
        directions: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val nameValidation = Validators.validateName(name)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(nameValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val nameValidation = Validators.validateName(lastName)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(nameValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val emailValidation = Validators.validateEmail(email)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(emailValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val passwordValidation = Validators.validatePassword(password)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(passwordValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            if (phone.isNotBlank()) {
                when (val phoneValidation = Validators.validatePhone(phone)) {
                    is Validators.ValidationResult.Error -> {
                        _authState.value = AuthState.Error(phoneValidation.message)
                        return@launch
                    }
                    is Validators.ValidationResult.Success -> {}
                }
            }

            val addressObj = Address(
                department = department,
                address = address,
                zone = zone,
                directions = directions,
                isDefault = true
            )

            repository.registerClient(name, lastName, email, password, phone, addressObj).fold(
                onSuccess = { userId ->
                    loadUserData(userId)
                    _authState.value = AuthState.Success(userId)
                    _message.emit("Cuenta creada exitosamente!")
                },
                onFailure = { error ->
                    val errorMsg = when {
                        error.message?.contains("already in use") == true -> ErrorMessages.EMAIL_ALREADY_IN_USE
                        error.message?.contains("password") == true -> ErrorMessages.WEAK_PASSWORD
                        error.message?.contains("email") == true -> ErrorMessages.INVALID_EMAIL
                        error.message?.contains("network") == true -> ErrorMessages.NETWORK_ERROR
                        else -> ErrorMessages.GENERIC_ERROR
                    }
                    _authState.value = AuthState.Error(errorMsg)
                    _message.emit(errorMsg)
                }
            )
        }
    }

    fun registerProvider(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        contactPreferences: List<String>,
        about: String,
        skills: List<String>
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val nameValidation = Validators.validateName(name)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(nameValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val nameValidation = Validators.validateName(lastName)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(nameValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val emailValidation = Validators.validateEmail(email)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(emailValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            when (val passwordValidation = Validators.validatePassword(password)) {
                is Validators.ValidationResult.Error -> {
                    _authState.value = AuthState.Error(passwordValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            if (phone.isNotBlank()) {
                when (val phoneValidation = Validators.validatePhone(phone)) {
                    is Validators.ValidationResult.Error -> {
                        _authState.value = AuthState.Error(phoneValidation.message)
                        return@launch
                    }
                    is Validators.ValidationResult.Success -> {}
                }
            }

            if (skills.isEmpty()) {
                _authState.value = AuthState.Error("Selecciona al menos una habilidad")
                return@launch
            }

            repository.registerProvider(
                name, lastName, email, password, phone,
                contactPreferences, about, skills
            ).fold(
                onSuccess = { userId ->
                    loadUserData(userId)
                    _authState.value = AuthState.Success(userId)
                    _message.emit("Cuenta de proveedor creada!")
                },
                onFailure = { error ->
                    val errorMsg = when {
                        error.message?.contains("already in use") == true -> ErrorMessages.EMAIL_ALREADY_IN_USE
                        error.message?.contains("password") == true -> ErrorMessages.WEAK_PASSWORD
                        error.message?.contains("email") == true -> ErrorMessages.INVALID_EMAIL
                        error.message?.contains("network") == true -> ErrorMessages.NETWORK_ERROR
                        else -> ErrorMessages.GENERIC_ERROR
                    }
                    _authState.value = AuthState.Error(errorMsg)
                    _message.emit(errorMsg)
                }
            )
        }
    }

    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            repository.getUserData(userId).fold(
                onSuccess = { user ->
                    _currentUser.value = user
                },
                onFailure = { error ->
                    _message.emit("Error al cargar datos del usuario")
                }
            )
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
        _currentUser.value = null
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            when (val emailValidation = Validators.validateEmail(email)) {
                is Validators.ValidationResult.Error -> {
                    _message.emit(emailValidation.message)
                    return@launch
                }
                is Validators.ValidationResult.Success -> {}
            }

            repository.resetPassword(email).fold(
                onSuccess = {
                    _message.emit("Correo de recuperación enviado")
                },
                onFailure = {
                    _message.emit("Error al enviar correo de recuperación")
                }
            )
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}