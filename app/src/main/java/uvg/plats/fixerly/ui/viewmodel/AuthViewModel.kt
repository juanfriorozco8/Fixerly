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

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    private var tempRegistrationData: TempRegistrationData? = null


    data class TempRegistrationData(
        val name: String,
        val lastName: String,
        val email: String,
        val password: String,
        val phone: String
    )


    fun checkUserLoggedIn() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val firebaseUser = repository.getCurrentUser()
            if (firebaseUser != null) {
                repository.getUserData(firebaseUser.uid).fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                        _authState.value = AuthState.Success(firebaseUser.uid)
                    },
                    onFailure = { error ->
                        repository.logout()
                        _authState.value = AuthState.Idle
                        _currentUser.value = null
                    }
                )
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Completa todos los campos")
                return@launch
            }

            repository.login(email, password).fold(
                onSuccess = { userId ->
                    repository.getUserData(userId).fold(
                        onSuccess = { user ->
                            _currentUser.value = user
                            _authState.value = AuthState.Success(userId)
                            _message.emit("Bienvenido!")
                        },
                        onFailure = { error ->
                            _authState.value = AuthState.Error("Error al cargar datos del usuario")
                            _message.emit("Error al cargar datos del usuario")
                        }
                    )
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


    fun saveTempRegistrationData(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (name.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Completa todos los campos obligatorios")
                _message.emit("Completa todos los campos obligatorios")
                return@launch
            }

            if (password.length < 6) {
                _authState.value = AuthState.Error(ErrorMessages.WEAK_PASSWORD)
                _message.emit(ErrorMessages.WEAK_PASSWORD)
                return@launch
            }

            tempRegistrationData = TempRegistrationData(
                name = name,
                lastName = lastName,
                email = email,
                password = password,
                phone = phone
            )

            _authState.value = AuthState.Success("")
            _message.emit("Datos guardados. Selecciona tu tipo de cuenta")
        }
    }


    fun registerClient(
        department: String,
        address: String,
        zone: String,
        directions: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val tempData = tempRegistrationData
            if (tempData == null) {
                _authState.value = AuthState.Error("Error: datos de registro no encontrados")
                _message.emit("Error: datos de registro no encontrados")
                return@launch
            }

            val addressObj = Address(
                department = department,
                address = address,
                zone = zone,
                directions = directions
            )

            repository.registerClient(
                name = tempData.name,
                lastName = tempData.lastName,
                email = tempData.email,
                password = tempData.password,
                phone = tempData.phone,
                address = addressObj
            ).fold(
                onSuccess = { userId ->
                    repository.getUserData(userId).fold(
                        onSuccess = { user ->
                            _currentUser.value = user
                            _authState.value = AuthState.Success(userId)
                            _message.emit("Cuenta creada exitosamente!")
                            tempRegistrationData = null
                        },
                        onFailure = { error ->
                            _authState.value = AuthState.Error("Error al cargar datos del usuario")
                            _message.emit("Error al cargar datos del usuario")
                        }
                    )
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
        contactPreferences: List<String>,
        about: String,
        skills: List<String>
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val tempData = tempRegistrationData
            if (tempData == null) {
                _authState.value = AuthState.Error("Error: datos de registro no encontrados")
                _message.emit("Error: datos de registro no encontrados")
                return@launch
            }

            if (skills.isEmpty()) {
                _authState.value = AuthState.Error("Selecciona al menos una habilidad")
                _message.emit("Selecciona al menos una habilidad")
                return@launch
            }

            repository.registerProvider(
                name = tempData.name,
                lastName = tempData.lastName,
                email = tempData.email,
                password = tempData.password,
                phone = tempData.phone,
                contactPreferences = contactPreferences,
                about = about,
                skills = skills
            ).fold(
                onSuccess = { userId ->
                    repository.getUserData(userId).fold(
                        onSuccess = { user ->
                            _currentUser.value = user
                            _authState.value = AuthState.Success(userId)
                            _message.emit("Cuenta de proveedor creada!")
                            tempRegistrationData = null
                        },
                        onFailure = { error ->
                            _authState.value = AuthState.Error("Error al cargar datos del usuario")
                            _message.emit("Error al cargar datos del usuario")
                        }
                    )
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
        tempRegistrationData = null
    }

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

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}