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

    // Lista de solicitudes pendientes (para proveedores)
    private val _pendingRequests = MutableStateFlow<UiState<List<ServiceRequest>>>(UiState())
    val pendingRequests = _pendingRequests.asStateFlow()

    // Lista de solicitudes del cliente
    private val _clientRequests = MutableStateFlow<UiState<List<ServiceRequest>>>(UiState())
    val clientRequests = _clientRequests.asStateFlow()

    // Estado de operaciones (crear, responder, aceptar, etc.)
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    // Mensajes
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    /**
     * Cargar todas las solicitudes pendientes (PROVEEDOR)
     */
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

    /**
     * Cargar solicitudes de un cliente específico
     */
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

    /**
     * Crear nueva solicitud de servicio (CLIENTE)
     */
    fun createServiceRequest(
        clientId: String,
        clientName: String,
        serviceType: String,
        description: String,
        address: Address
    ) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            // Validaciones
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

    /**
     * Proveedor responde a una solicitud
     */
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

    /**
     * Cliente acepta un proveedor
     */
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

    /**
     * Cliente rechaza un proveedor
     */
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

    /**
     * Marcar solicitud como completada
     */
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

    /**
     * Eliminar solicitud
     */
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

    /**
     * Reset de estado de operación
     */
    fun resetOperationState() {
        _operationState.value = OperationState.Idle
    }

    /**
     * Retry para cargar solicitudes
     */
    fun retryLoadRequests(isProvider: Boolean, clientId: String? = null) {
        if (isProvider) {
            loadPendingRequests()
        } else if (clientId != null) {
            loadClientRequests(clientId)
        }
    }
}