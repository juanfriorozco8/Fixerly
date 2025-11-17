package uvg.plats.fixerly.ui.navigation

import kotlinx.serialization.Serializable


// === PRUEBA DE DATABASE (TEMPORAL) ===
//@Serializable
//data object TestDatabaseDestination


// flujo de la autenticacion, login y eso
@Serializable
data object OnboardingDestination

@Serializable
data object LoginDestination

// Flujo de registro completo (ruta padre)
@Serializable
data object RegisterDestination

// Primera pantalla del registro
@Serializable
data object RegisterStepDestination

@Serializable
data object AccountTypeDestination

@Serializable
data object AddressDestination

@Serializable
data class SupplierDataDestination(
    val accountType: String = "Proveedor"
)


@Serializable
data object LaborDestination

@Serializable
data object YourRequestsDestination

@Serializable
data object UserProfileDestination


@Serializable
data object SupplierWelcomeDestination

@Serializable
data object SupplierProfileDestination