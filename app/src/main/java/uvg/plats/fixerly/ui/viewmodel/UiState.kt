package uvg.plats.fixerly.ui.viewmodel

/**
 * Estado de autenticación
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

/**
 * Estado genérico de UI para datos
 */
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Estado de operaciones (crear, actualizar, eliminar)
 */
sealed class OperationState {
    data object Idle : OperationState()
    data object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}