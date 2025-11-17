package uvg.plats.fixerly.ui.viewmodel

/**
 * Estado genérico para manejar UI en ViewModels
 * Basado en tu ejemplo de lab8
 */
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Estados específicos para autenticación
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

/**
 * Estados para operaciones CRUD
 */
sealed class OperationState {
    object Idle : OperationState()
    object Loading : OperationState()
    data class Success(val message: String = "Operación exitosa") : OperationState()
    data class Error(val message: String) : OperationState()
}