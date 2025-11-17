package uvg.plats.fixerly.utils

import android.util.Patterns

object Validators {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidGuatemalanPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[\\s-]"), "")
        return cleanPhone.matches(Regex("^(\\+502)?[2-9]\\d{7}$"))
    }

    fun isFieldEmpty(field: String): Boolean {
        return field.isBlank()
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword && password.isNotBlank()
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            isFieldEmpty(email) -> ValidationResult.Error("El correo es requerido")
            !isValidEmail(email) -> ValidationResult.Error("Correo electrónico inválido")
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            isFieldEmpty(password) -> ValidationResult.Error("La contraseña es requerida")
            !isValidPassword(password) -> ValidationResult.Error("La contraseña debe tener al menos 6 caracteres")
            else -> ValidationResult.Success
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        return when {
            isFieldEmpty(phone) -> ValidationResult.Error("El teléfono es requerido")
            !isValidGuatemalanPhone(phone) -> ValidationResult.Error("Número de teléfono inválido")
            else -> ValidationResult.Success
        }
    }

    fun validatePasswordConfirmation(password: String, confirmPassword: String): ValidationResult {
        return when {
            isFieldEmpty(confirmPassword) -> ValidationResult.Error("Por favor confirma tu contraseña")
            !doPasswordsMatch(password, confirmPassword) -> ValidationResult.Error("Las contraseñas no coinciden")
            else -> ValidationResult.Success
        }
    }

    fun validateName(name: String): ValidationResult {
        return when {
            isFieldEmpty(name) -> ValidationResult.Error("El nombre es requerido")
            name.length < 2 -> ValidationResult.Error("El nombre debe tener al menos 2 caracteres")
            else -> ValidationResult.Success
        }
    }

    sealed class ValidationResult {
        data object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
}