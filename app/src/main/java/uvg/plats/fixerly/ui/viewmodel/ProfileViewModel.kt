package uvg.plats.fixerly.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.data.repository.ProfileRepository

class ProfileViewModel : ViewModel() {

    // este se conecta al repo del perfil
    private val repository = ProfileRepository()

    // val que sirve para contener el profile y hacerlo mutable (puede cambiar con el tiempo)
    private val _userProfile = MutableStateFlow<UiState<User>>(UiState())
    val userProfile = _userProfile.asStateFlow()

    // contiene el estado de la operacion y lo hace mutable 
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    // mensaje del perfil que se hace mutable 
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    // la funcion sirve para cargar el perfil del usuario
    // el value del perfil de usuario se actualiza segun el estado de la ui, si está cargando o no
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _userProfile.value = UiState(isLoading = true)

            repository.getUserProfile(userId).fold(
                onSuccess = { user ->
                    _userProfile.value = UiState(
                        isLoading = false,
                        data = user
                    )
                },
                onFailure = { error ->
                    _userProfile.value = UiState(
                        isLoading = false,
                        hasError = true,
                        errorMessage = error.message ?: "Error al cargar perfil"
                    )
                    _message.emit("Error al cargar perfil: ${error.message}")
                }
            )
        }
    }

    
    fun updateBasicInfo(
        userId: String,
        name: String,
        lastName: String,
        phone: String
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            if (name.isBlank() || lastName.isBlank()) {
                _operationState.value = OperationState.Error("Completa todos los campos")
                _message.emit("Completa todos los campos")
                return@launch
            }

            repository.updateBasicInfo(userId, name, lastName, phone).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Perfil actualizado")
                    _message.emit("Perfil actualizado exitosamente")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error al actualizar: ${error.message}")
                }
            )
        }
    }


    fun updateClientAddress(
        userId: String,
        address: Address
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.updateClientAddress(userId, address).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Dirección actualizada")
                    _message.emit("Dirección actualizada")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error: ${error.message}")
                }
            )
        }
    }


    fun updateProviderSkills(
        userId: String,
        skills: List<String>
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            if (skills.isEmpty()) {
                _operationState.value = OperationState.Error("Selecciona al menos una habilidad")
                _message.emit("Selecciona al menos una habilidad")
                return@launch
            }

            repository.updateProviderSkills(userId, skills).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Habilidades actualizadas")
                    _message.emit("Habilidades actualizadas")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error: ${error.message}")
                }
            )
        }
    }


    fun updateProviderAbout(
        userId: String,
        about: String
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.updateProviderAbout(userId, about).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Descripción actualizada")
                    _message.emit("Descripción actualizada")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error: ${error.message}")
                }
            )
        }
    }


    fun updateContactPreferences(
        userId: String,
        preferences: List<String>
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.updateContactPreferences(userId, preferences).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Preferencias actualizadas")
                    _message.emit("Preferencias de contacto actualizadas")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error: ${error.message}")
                }
            )
        }
    }


    fun uploadProfileImage(
        userId: String,
        imageUri: Uri
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.uploadProfileImage(userId, imageUri).fold(
                onSuccess = { downloadUrl ->
                    _operationState.value = OperationState.Success("Foto actualizada")
                    _message.emit("Foto de perfil actualizada")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error al subir foto: ${error.message}")
                }
            )
        }
    }

    fun deleteProfileImage(userId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.deleteProfileImage(userId).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Foto eliminada")
                    _message.emit("Foto de perfil eliminada")
                    loadUserProfile(userId)
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error al eliminar foto: ${error.message}")
                }
            )
        }
    }


    fun searchProvidersBySkill(skill: String) {
        viewModelScope.launch {
            repository.getProvidersBySkill(skill).fold(
                onSuccess = { providers ->
                    _message.emit("${providers.size} proveedores encontrados")
                },
                onFailure = { error ->
                    _message.emit("Error en búsqueda: ${error.message}")
                }
            )
        }
    }


    fun resetOperationState() {
        _operationState.value = OperationState.Idle
    }


    fun retryLoadProfile(userId: String) {
        loadUserProfile(userId)
    }
}