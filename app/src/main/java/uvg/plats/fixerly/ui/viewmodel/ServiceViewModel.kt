package uvg.plats.fixerly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.ServiceRequest
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.data.repository.ServiceRepository

class ServiceViewModel : ViewModel() {
    private val repository = ServiceRepository()

    private val _pendingRequests = MutableStateFlow<UiState<List<ServiceRequest>>>(UiState())
    val pendingRequests = _pendingRequests.asStateFlow()

    private val _clientRequests = MutableStateFlow<UiState<List<ServiceRequest>>>(UiState())
    val clientRequests = _clientRequests.asStateFlow()

    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()


    fun loadPendingRequests() {
        viewModelScope.launch {
            _pendingRequests.value = UiState(isLoading = true)
            try {
                repository.getAllPendingRequests().collect { requests ->
                    _pendingRequests.value = UiState(
                        isLoading = false,
                        data = requests
                    )
                }
            } catch (e: Exception) {
                _pendingRequests.value = UiState(
                    isLoading = false,
                    hasError = true,
                    errorMessage = e.message ?: "Error al cargar solicitudes"
                )
            }
        }
    }


    fun loadClientRequests(clientId: String) {
        viewModelScope.launch {
            _clientRequests.value = UiState(isLoading = true)
            try {
                repository.getClientRequests(clientId).collect { requests ->
                    _clientRequests.value = UiState(
                        isLoading = false,
                        data = requests
                    )
                }
            } catch (e: Exception) {
                _clientRequests.value = UiState(
                    isLoading = false,
                    hasError = true,
                    errorMessage = e.message ?: "Error al cargar tus solicitudes"
                )
            }
        }
    }


    fun createServiceRequest(
        clientId: String,
        clientName: String,
        serviceType: String,
        description: String,
        address: Address
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            if (serviceType.isBlank() || description.isBlank()) {
                _operationState.value = OperationState.Error("Completa todos los campos")
                _message.emit("Completa todos los campos")
                return@launch
            }

            repository.createServiceRequest(
                clientId, clientName, serviceType, description, address
            ).fold(
                onSuccess = { requestId ->
                    _operationState.value = OperationState.Success("Solicitud creada exitosamente")
                    _message.emit("Solicitud creada! Los proveedores podrán verla.")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error al crear solicitud")
                    _message.emit("Error al crear solicitud: ${error.message}")
                }
            )
        }
    }

    fun respondToRequest(
        requestId: String,
        providerUser: User,
        message: String = ""
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.respondToRequest(requestId, providerUser, message).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Respuesta enviada")
                    this@ServiceViewModel._message.emit("Tu información fue enviada al cliente")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error al responder")
                    this@ServiceViewModel._message.emit("Error al enviar respuesta: ${error.message}")
                }
            )
        }
    }


    fun acceptProvider(requestId: String, providerId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.acceptProviderResponse(requestId, providerId).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Proveedor aceptado")
                    _message.emit("Proveedor aceptado! Revisa su información de contacto.")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error al aceptar")
                    _message.emit("Error al aceptar proveedor: ${error.message}")
                }
            )
        }
    }


    fun rejectProvider(requestId: String, providerId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.rejectProviderResponse(requestId, providerId).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Proveedor rechazado")
                    _message.emit("Respuesta rechazada")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error al rechazar")
                    _message.emit("Error al rechazar: ${error.message}")
                }
            )
        }
    }


    fun completeRequest(requestId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.completeRequest(requestId).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Solicitud completada")
                    _message.emit("Solicitud marcada como completada")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error: ${error.message}")
                }
            )
        }
    }

    fun deleteRequest(requestId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            repository.deleteRequest(requestId).fold(
                onSuccess = {
                    _operationState.value = OperationState.Success("Solicitud eliminada")
                    _message.emit("Solicitud eliminada")
                },
                onFailure = { error ->
                    _operationState.value = OperationState.Error(error.message ?: "Error")
                    _message.emit("Error al eliminar: ${error.message}")
                }
            )
        }
    }

    fun resetOperationState() {
        _operationState.value = OperationState.Idle
    }


    fun retryLoadRequests(isProvider: Boolean, clientId: String? = null) {
        if (isProvider) {
            loadPendingRequests()
        } else if (clientId != null) {
            loadClientRequests(clientId)
        }
    }
}