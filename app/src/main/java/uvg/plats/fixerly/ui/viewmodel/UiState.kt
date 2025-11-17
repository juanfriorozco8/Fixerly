package uvg.plats.fixerly.ui.viewmodel


sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}


data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)


sealed class OperationState {
    data object Idle : OperationState()
    data object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}