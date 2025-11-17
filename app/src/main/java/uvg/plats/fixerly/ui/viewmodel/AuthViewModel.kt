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

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    // Estado de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    // Estado del usuario actual
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Mensajes de error/éxito (para mostrar Snackbar/Toast)
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        checkUserLoggedIn()
    }

    /**
     * Verificar si hay usuario logueado al iniciar
     */
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            val firebaseUser = repository.getCurrentUser()
            if (firebaseUser != null) {
                loadUserData(firebaseUser.uid)
            }
        }
    }

    /**
     * Login
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // Validaciones básicas
            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Completa todos los campos")
                return@launch
            }

            // Llamar al repository
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

    /**
     * Registro de CLIENTE
     */
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

            // Validaciones
            if (name.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Completa todos los campos obligatorios")
                return@launch
            }

            if (password.length < 6) {
                _authState.value = AuthState.Error(ErrorMessages.WEAK_PASSWORD)
                return@launch
            }

            val addressObj = Address(department, address, zone, directions)

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

    /**
     * Registro de PROVEEDOR
     */
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

            // Validaciones
            if (name.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Completa todos los campos obligatorios")
                return@launch
            }

            if (password.length < 6) {
                _authState.value = AuthState.Error(ErrorMessages.WEAK_PASSWORD)
                return@launch
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

    /**
     * Cargar datos del usuario
     */
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

    /**
     * Logout
     */
    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
        _currentUser.value = null
    }

    /**
     * Recuperar contraseña
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _message.emit("Ingresa tu correo electrónico")
                return@launch
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

    /**
     * Reset del estado (útil para limpiar errores)
     */
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}